#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;
uniform float TripTime;
uniform float Intensity;
uniform float TripSeed;

out vec4 fragColor;

// Noise functions
float hash(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 45.32);
    return fract(p.x * p.y);
}

float noise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);
    f = f * f * (3.0 - 2.0 * f);

    float a = hash(i);
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));

    return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
}

float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.5;
    float frequency = 1.0;

    for(int i = 0; i < 6; i++) {
        value += amplitude * noise(p * frequency);
        frequency *= 2.0;
        amplitude *= 0.5;
    }
    return value;
}

vec3 hsv2rgb(vec3 c) {
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

vec3 rgb2hsv(vec3 c) {
    vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);
    vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));
    vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));

    float d = q.x - min(q.w, q.y);
    float e = 1.0e-10;
    return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);
}

void main() {
    vec2 uv = texCoord;
    float t = TripTime * 0.5;
    float seed = TripSeed;

    float pulse = 0.5 + 0.5 * sin(t * 0.3);
    float baseIntensity = Intensity * (0.7 + 0.3 * pulse);

    vec2 distortedUV = uv;
    float waveIntensity = baseIntensity * 0.7;

    if (waveIntensity > 0.01) {
        float waveFreq = 8.0 + baseIntensity * 12.0;
        float waveSpeed = t * 0.8;

        float waveX = sin(uv.y * waveFreq + waveSpeed) * 0.01 * waveIntensity;
        waveX += sin(uv.y * waveFreq * 1.7 + waveSpeed * 1.3) * 0.007 * waveIntensity;

        float waveY = cos(uv.x * waveFreq * 0.7 + waveSpeed * 0.9) * 0.01 * waveIntensity;
        waveY += cos(uv.x * waveFreq * 1.4 + waveSpeed * 1.1) * 0.008 * waveIntensity;

        vec2 centerVec = uv - 0.5;
        float dist = length(centerVec);
        float radialWave = sin(dist * 15.0 - t * 1.5) * 0.008 * waveIntensity;

        distortedUV.x += waveX + centerVec.x * radialWave;
        distortedUV.y += waveY + centerVec.y * radialWave;
    }

    vec3 col;
    float tracerAmount = smoothstep(0.1, 0.5, baseIntensity) * 0.8;

    if (tracerAmount > 0.01) {
        vec3 currentColor = texture(DiffuseSampler, distortedUV).rgb;

        vec2 tracerOffset = vec2(
        sin(t * 0.7 + seed) * 0.008 * tracerAmount,
        cos(t * 0.5 + seed * 1.3) * 0.008 * tracerAmount
        );
        vec3 previousColor = texture(DiffuseSampler, distortedUV + tracerOffset).rgb;

        float blendFactor = 0.3 + 0.4 * sin(t * 0.4);
        col = mix(currentColor, mix(currentColor, previousColor, 0.7), tracerAmount * blendFactor);
    } else {
        col = texture(DiffuseSampler, distortedUV).rgb;
    }

    float chromaAmount = baseIntensity * 0.015;
    if (chromaAmount > 0.001) {
        vec2 chromaDir = normalize(uv - 0.5);
        float chromaPulse = 0.5 + 0.5 * sin(t * 0.9);

        vec2 rOffset = chromaDir * chromaAmount * 0.5 * chromaPulse;
        vec2 gOffset = chromaDir * chromaAmount * -0.3 * chromaPulse;
        vec2 bOffset = chromaDir * chromaAmount * 0.7 * chromaPulse;

        float r = texture(DiffuseSampler, distortedUV + rOffset).r;
        float g = texture(DiffuseSampler, distortedUV + gOffset).g;
        float b = texture(DiffuseSampler, distortedUV + bOffset).b;

        col = vec3(r, g, b);
    }

    float colorShiftAmount = baseIntensity * 0.6;
    if (colorShiftAmount > 0.01) {
        vec3 hsv = rgb2hsv(col);

        float hueShift = sin(t * 0.4) * 0.15 + sin(t * 0.2) * 0.1;
        hsv.x = fract(hsv.x + hueShift * colorShiftAmount);

        float saturationBoost = 1.0 + colorShiftAmount * 0.8;
        hsv.y = min(hsv.y * saturationBoost, 0.95);

        float brightnessPulse = 0.9 + 0.2 * sin(t * 0.5);
        hsv.z *= brightnessPulse;

        col = hsv2rgb(hsv);
    }

    float patternIntensity = smoothstep(0.3, 0.8, baseIntensity) * 0.5;
    if (patternIntensity > 0.01) {
        vec2 patternUV = uv * (4.0 + baseIntensity * 8.0);
        float timeOffset = t * 0.2;

        float organic = fbm(patternUV * 0.65 + vec2(timeOffset, -timeOffset * 0.8));
        float pattern = organic;

        vec3 patternColor = hsv2rgb(vec3(fract(t * 0.05 + baseIntensity), 0.7, 1.0));
        col = mix(col, patternColor, pattern * patternIntensity * 0.3);
    }

    float snowIntensity = smoothstep(0.5, 0.9, baseIntensity) * 0.2;
    if (snowIntensity > 0.01) {
        float snow = hash(uv * 100.0 + t) * 2.0 - 1.0;
        col += vec3(snow * snowIntensity * 0.3);

        float coloredNoise = fbm(uv * 50.0 + t * 5.0) * snowIntensity * 0.4;
        vec3 noiseColor = hsv2rgb(vec3(fract(t * 0.1), 0.6, coloredNoise));
        col = mix(col, col + noiseColor, snowIntensity * 0.2);
    }

    float bloomIntensity = smoothstep(0.2, 0.6, baseIntensity) * 0.3;
    if (bloomIntensity > 0.01) {
        float bloomRadius = 0.002 + baseIntensity * 0.008;
        vec3 bloom = vec3(0.0);
        float samples = 0.0;

        for(float x = -1.0; x <= 1.0; x += 2.0) {
            for(float y = -1.0; y <= 1.0; y += 2.0) {
                bloom += texture(DiffuseSampler, distortedUV + vec2(x, y) * bloomRadius).rgb;
                samples += 1.0;
            }
        }
        bloom /= samples;

        float luminance = dot(bloom, vec3(0.299, 0.587, 0.114));
        float glowFactor = smoothstep(0.6, 0.9, luminance) * bloomIntensity;

        vec3 glowColor = hsv2rgb(vec3(fract(t * 0.07), 0.8, 1.0));
        col = mix(col, col + bloom * glowColor, glowFactor);
    }

    float timeWarpIntensity = smoothstep(0.7, 1.0, baseIntensity) * 0.4;
    if (timeWarpIntensity > 0.01) {
        float timePulse = sin(t * 0.6) * 0.5 + 0.5;

        float skipChance = hash(vec2(t * 0.1, seed)) * timeWarpIntensity;
        if (skipChance > 0.95) {
            col *= 1.5;
        } else if (skipChance > 0.9) {
            col *= 0.7;
        }

        col *= 0.9 + 0.2 * sin(t * 0.2) * timeWarpIntensity;
    }

    float edgeIntensity = smoothstep(0.4, 0.8, baseIntensity) * 0.25;
    if (edgeIntensity > 0.01) {
        vec3 top = texture(DiffuseSampler, distortedUV + vec2(0.0, oneTexel.y)).rgb;
        vec3 bottom = texture(DiffuseSampler, distortedUV + vec2(0.0, -oneTexel.y)).rgb;
        vec3 left = texture(DiffuseSampler, distortedUV + vec2(-oneTexel.x, 0.0)).rgb;
        vec3 right = texture(DiffuseSampler, distortedUV + vec2(oneTexel.x, 0.0)).rgb;

        vec3 horizEdge = right - left;
        vec3 vertEdge = top - bottom;
        float edge = length(horizEdge) + length(vertEdge);

        vec3 edgeColor = hsv2rgb(vec3(fract(t * 0.15), 0.9, 1.0));
        col = mix(col, col + edgeColor, edge * edgeIntensity);

        float contrast = 1.0 + edgeIntensity * 0.3;
        col = (col - 0.5) * contrast + 0.5;
    }

    float depthIntensity = baseIntensity * 0.3;
    if (depthIntensity > 0.01) {

        float depthWarp = sin((uv.x + uv.y) * 10.0 + t) * depthIntensity * 0.02;
        col *= 1.0 + depthWarp;

    }

    float globalPulse = 0.95 + 0.1 * sin(t * 0.4);
    col *= globalPulse;

    col = clamp(col, 0.0, 1.0);

    if (baseIntensity > 0.95) {
        float overload = smoothstep(0.95, 1.0, baseIntensity);
        col *= 1.0 - overload * 0.5;

        // White flashes at peak intensity
        float flash = step(0.995, hash(vec2(t * 10.0, seed)));
        if (flash > 0.5) {
            col = vec3(1.0);
        }
    }

    fragColor = vec4(col, 1.0);
}
