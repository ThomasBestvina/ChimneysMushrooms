#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;
uniform float TripTime;
uniform float Intensity;
uniform float TripSeed;

out vec4 fragColor;

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

    for(int i = 0; i < 5; i++) {
        value += amplitude * noise(p * frequency);
        frequency *= 2.0;
        amplitude *= 0.5;
    }
    return value;
}

float mandelbrot(vec2 c, int maxIter) {
    vec2 z = vec2(0.0);
    float iter = 0.0;

    for(int i = 0; i < maxIter; i++) {
        if(length(z) > 2.0) break;
        z = vec2(z.x * z.x - z.y * z.y, 2.0 * z.x * z.y) + c;
        iter += 1.0;
    }

    return iter / float(maxIter);
}

float julia(vec2 z, vec2 c, int maxIter) {
    float iter = 0.0;

    for(int i = 0; i < maxIter; i++) {
        if(length(z) > 2.0) break;
        z = vec2(z.x * z.x - z.y * z.y, 2.0 * z.x * z.y) + c;
        iter += 1.0;
    }

    return iter / float(maxIter);
}

float circleFractal(vec2 p, float t) {
    float pattern = 0.0;
    float scale = 1.0;

    for(int i = 0; i < 4; i++) {
        vec2 pos = fract(p * scale) - 0.5;
        float d = length(pos);
        pattern += smoothstep(0.3, 0.25, d) / scale;
        scale *= 2.5 + sin(t * 0.3) * 0.5;
        p = p * 2.0 + vec2(sin(t * 0.2), cos(t * 0.15));
    }

    return pattern;
}

float sierpinski(vec2 p, int iterations) {
    float pattern = 1.0;

    for(int i = 0; i < iterations; i++) {
        p = abs(p);
        if(p.x + p.y > 1.0) {
            p = vec2(1.0 - p.y, 1.0 - p.x);
        }
        p = p * 2.0 - 0.5;
        pattern *= 0.5;
    }

    return pattern;
}

vec2 kaleidoscope(vec2 uv, float segments, float t) {
    float angle = atan(uv.y, uv.x);
    float radius = length(uv);
    angle = mod(angle, 3.14159 * 2.0 / segments);
    angle = abs(angle - 3.14159 / segments);
    return vec2(cos(angle), sin(angle)) * radius;
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
    float t = TripTime * 0.25;
    vec2 center = uv - 0.5;

    float overallMultiplier = Intensity * Intensity;

    float distortionAmount = smoothstep(0.0, 0.3, Intensity) * overallMultiplier*1.5;
    float hueShiftAmount = smoothstep(0.1, 0.4, Intensity) * overallMultiplier;
    float fractalAmount = smoothstep(0.4, 0.8, Intensity) * overallMultiplier;
    float fullEffectAmount = smoothstep(0.7, 1.0, Intensity) * overallMultiplier;
    // Delay color warping so the shader ramps into color instead of snapping immediately.
    float colorPhase = smoothstep(0.55, 0.9, Intensity);

    float ultraStart = smoothstep(0.0, 0.15, Intensity);
    distortionAmount *= ultraStart;
    hueShiftAmount *= ultraStart * ultraStart;

    vec2 finalUV = uv;
    if (distortionAmount > 0.0) {
        float breathe = sin(t * 0.5) * 0.5 + 0.5;
        float dist = length(center);
        float gentleWarp = sin(dist * 5.0 - t) * 0.005 * breathe * distortionAmount;
        vec2 warpOffset = normalize(center) * gentleWarp;
        finalUV = uv + warpOffset;
    }

    vec3 col = texture(DiffuseSampler, finalUV).rgb;

    if (fractalAmount > 0.0) {
        float current1 = fbm(uv * 2.0 + vec2(t * 0.1, -t * 0.08));
        float current2 = fbm(uv * 3.0 - vec2(t * 0.12, t * 0.15));
        float current3 = fbm(uv * 1.5 + vec2(sin(t * 0.3), cos(t * 0.25)));

        float slowT = t * 0.1 + TripSeed * 0.1;
        float seed1 = sin(slowT * 1.3 + TripSeed * 0.537);
        float seed2 = cos(slowT * 1.7 + TripSeed * 0.891);
        float seed3 = sin(slowT * 2.1 + TripSeed * 1.234);
        float seed4 = cos(slowT * 0.9 + TripSeed * 0.678);

        float kaleidoSegments = 6.0 + sin(t * 0.3 + TripSeed * 0.432) * 2.0 + sin(TripSeed * 1.123) * 2.0;
        vec2 kaleidoUV = kaleidoscope(center, kaleidoSegments, t);

        vec2 juliaC1 = vec2(
        -0.4 + sin(t * 0.53 + seed1 * 0.5 + TripSeed * 0.234) * 0.3,
        0.6 + cos(t * 0.37 + seed2 * 0.5 + TripSeed * 0.567) * 0.3
        );
        vec2 juliaC2 = vec2(
        0.285 + sin(t * 0.41 + seed3 * 0.5 + TripSeed * 0.789) * 0.2,
        0.01 + cos(t * 0.29 + seed4 * 0.5 + TripSeed * 0.345) * 0.2
        );
        vec2 juliaC3 = vec2(
        -0.7 + cos(t * 0.6 + TripSeed * 0.912) * 0.3,
        0.27 + sin(t * 0.5 + TripSeed * 0.123) * 0.25
        );

        vec2 fractalCenter1 = kaleidoUV + vec2(
        sin(t * 0.3 + TripSeed * 0.456) * 0.3,
        cos(t * 0.4 + TripSeed * 0.234) * 0.3
        );
        vec2 fractalCenter2 = center + vec2(
        cos(t * 0.25 + TripSeed * 0.789) * 0.4,
        sin(t * 0.35 + TripSeed * 0.567) * 0.3
        );
        vec2 fractalCenter3 = center + vec2(
        sin(t * 0.2 + TripSeed * 0.912) * 0.35,
        cos(t * 0.3 + TripSeed * 0.345) * 0.35
        );

        vec2 fractalUV1 = fractalCenter1 * (1.5 + sin(t * 0.4 + TripSeed * 0.678) * 0.5);
        vec2 fractalUV2 = fractalCenter2 * (2.2 + cos(t * 0.5 + TripSeed * 0.891) * 0.6);
        vec2 fractalUV3 = fractalCenter3 * (1.8 + sin(t * 0.35 + TripSeed * 0.123) * 0.5);

        float juliaPattern1 = julia(fractalUV1, juliaC1, 20);
        float juliaPattern2 = julia(fractalUV2, juliaC2, 25);
        float juliaPattern3 = julia(fractalUV3, juliaC3, 18);

        vec2 mandelbrotUV = kaleidoUV * (1.3 + sin(t * 0.6) * 0.4);
        float mandelbrotPattern = mandelbrot(mandelbrotUV, 20);

        vec2 sierpinskiUV1 = fract(uv * 3.0 + vec2(t * 0.08, -t * 0.06));
        vec2 sierpinskiUV2 = fract(kaleidoUV * 2.0 + 0.5);
        float sierpinskiPattern1 = sierpinski(sierpinskiUV1 * 2.0 - 1.0, 5);
        float sierpinskiPattern2 = sierpinski(sierpinskiUV2 * 2.0 - 1.0, 6);

        float layer1 = smoothstep(0.1, 0.9, juliaPattern1);
        float layer2 = smoothstep(0.2, 0.8, juliaPattern2);
        float layer3 = smoothstep(0.15, 0.85, juliaPattern3);
        float layer4 = smoothstep(0.3, 0.7, mandelbrotPattern);
        float layer7 = sierpinskiPattern1 * 2.0;
        float layer8 = sierpinskiPattern2 * 2.5;

        float mix1 = sin(t * 0.8) * 0.5 + 0.5;
        float mix2 = cos(t * 0.6) * 0.5 + 0.5;
        float mix3 = sin(t * 0.4) * 0.5 + 0.5;

        float fractalMix = mix(
        mix(layer1, layer2, mix1),
        mix(layer3, layer4, mix2),
        0.5
        );

        float noiseVariation = fbm(uv * 5.0 + t * 0.2);
        fractalMix = mix(fractalMix, noiseVariation, 0.1);

        float distortionScale = distortionAmount * 0.3 + fractalAmount * 0.7;

        vec2 flowField = vec2(
        sin(current1 * 6.28 + t + fractalMix * 3.14) * cos(current2 * 3.14),
        cos(current2 * 6.28 - t + fractalMix * 3.14) * sin(current3 * 3.14)
        ) * 0.03 * distortionScale * (1.0 + fractalMix * 0.5);

        float streamPattern = sin(uv.x * 10.0 + current1 * 5.0 + t + fractalMix * 2.0) *
        cos(uv.y * 8.0 + current2 * 4.0 - t);
        vec2 streamFlow = vec2(
        sin(streamPattern * 3.14 + t * 0.5),
        cos(streamPattern * 3.14 - t * 0.5)
        ) * 0.02 * distortionScale;

        float dist = length(center);
        float breathe = sin(t * 0.8 + fractalMix * 1.5) * 0.5 + 0.5;
        float morph = (1.0 + sin(dist * 8.0 - t * 1.5 + fractalMix * 2.0) * 0.1 * breathe) * fractalAmount * 0.02;
        vec2 morphOffset = normalize(center) * morph;

        float zoomMask = smoothstep(0.3, 0.7, fractalMix);
        float localZoom = 1.0 + zoomMask * sin(t * 0.4 + fractalMix * 3.0) * 0.3 * fractalAmount;
        vec2 zoomDistort = (uv - 0.5) / localZoom + 0.5;

        float bubble1 = smoothstep(0.3, 0.0, length(uv - vec2(0.3 + sin(t * 0.3) * 0.2, 0.5 + cos(t * 0.4) * 0.2)));
        float bubble2 = smoothstep(0.25, 0.0, length(uv - vec2(0.7 + cos(t * 0.25) * 0.2, 0.6 + sin(t * 0.35) * 0.2)));
        float zoomFactor = 1.0 + (bubble1 * 0.4 - bubble2 * 0.3) * fractalAmount;
        vec2 bubbleDistort = (uv - 0.5) / zoomFactor + 0.5;

        vec2 baseUV = finalUV + flowField * fractalAmount + streamFlow * fractalAmount + morphOffset;
        finalUV = mix(mix(baseUV, zoomDistort, 0.4 * fractalAmount), bubbleDistort, 0.6 * fractalAmount);

        float aberration = 0.004 * (1.0 + sin(t * 0.6 + fractalMix) * 0.5) * fractalAmount;
        vec2 aberrationOffset = normalize(center) * aberration;

        float r = texture(DiffuseSampler, finalUV + aberrationOffset * 1.5).r;
        float g = texture(DiffuseSampler, finalUV).g;
        float b = texture(DiffuseSampler, finalUV - aberrationOffset * 1.5).b;

        col = vec3(r, g, b);
        vec3 preColorWarp = col;
        vec3 hsv = rgb2hsv(col);

        float hueFlow = fbm(uv * 1.5 + t * 0.08) * 0.3 * hueShiftAmount * colorPhase;
        hsv.x = fract(hsv.x + (t * 0.08 * hueShiftAmount + hueFlow + fractalMix * 0.3 * fractalAmount) * colorPhase);

        float baseSat = hsv.y;
        hsv.y = baseSat * (1.0 + 0.5 * fractalAmount * (current1 * 0.5 + 0.5 + fractalMix * 0.5) * colorPhase);
        hsv.y = clamp(hsv.y, 0.0, 1.0);

        float brightnessPulse = 1.0 + sin(t * 0.5 + fractalMix * 0.5) * 0.15 * fractalAmount * colorPhase;
        hsv.z *= brightnessPulse;

        col = hsv2rgb(hsv);
        col = mix(preColorWarp, col, colorPhase);

        float hueOffset = fract(TripSeed * 0.618034);
        vec3 fractalColor1 = hsv2rgb(vec3(fract(t * 0.05 + layer1 * 0.3 + hueOffset), 0.8, layer1 * 0.6));
        vec3 fractalColor2 = hsv2rgb(vec3(fract(t * 0.07 + layer2 * 0.3 + hueOffset + 0.33), 0.7, layer2 * 0.5));
        vec3 fractalColor3 = hsv2rgb(vec3(fract(t * 0.06 + layer3 * 0.3 + hueOffset + 0.66), 0.75, layer3 * 0.55));
        vec3 fractalColor4 = hsv2rgb(vec3(fract(t * 0.04 + layer4 * 0.4 + hueOffset + 0.15), 0.65, layer4 * 0.5));
        vec3 sierpinskiColor = hsv2rgb(vec3(fract(t * 0.08 + hueOffset + 0.75), 0.85, layer7 * 0.6));

        float fractalStrength = 0.35 * fractalAmount * (0.5 + 0.5 * fullEffectAmount) * colorPhase;
        col = mix(col, fractalColor1, layer1 * fractalStrength * 0.4);
        col = mix(col, fractalColor2, layer2 * fractalStrength * 0.35);
        col = mix(col, fractalColor3, layer3 * fractalStrength * 0.3);
        col = mix(col, fractalColor4, layer4 * fractalStrength * 0.4);
        col = mix(col, sierpinskiColor, layer7 * fractalStrength * 0.45);

        float edge1 = abs(fract(layer1 * 8.0) - 0.5) * 2.0;
        float edge2 = abs(fract(layer2 * 10.0) - 0.5) * 2.0;
        float edgeGlow = (1.0 - edge1) * (1.0 - edge2) * 0.3 * fullEffectAmount;
        col += edgeGlow * hsv2rgb(vec3(fract(t * 0.15), 0.9, 0.8));

        float trail = fbm(uv * 3.0 - vec2(t * 0.2, -t * 0.15));
        col = mix(col, col * 1.2, trail * 0.15 * fractalAmount);

        float grain = (hash(uv * TripTime) - 0.5) * 0.015 * fractalAmount;
        col += grain;

        float vignette = smoothstep(0.9, 0.2, length(center));
        col *= 0.8 + 0.2 * vignette;
    }

    fragColor = vec4(col, 1.0);
}
