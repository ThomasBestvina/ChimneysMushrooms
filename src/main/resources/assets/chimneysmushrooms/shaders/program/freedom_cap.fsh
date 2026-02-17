#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;
uniform float TripTime;
uniform float Intensity;
uniform float TripSeed;
uniform float ShowFlag;

out vec4 fragColor;

float hash(vec2 p) {
    p = fract(p * vec2(123.34, 456.21));
    p += dot(p, p + 34.45);
    return fract(p.x * p.y);
}

float starMask(vec2 uv, vec2 center, float size) {
    vec2 p = (uv - center) / size;
    float r = length(p);
    float a = atan(p.y, p.x);

    float points = abs(cos(a * 5.0)) * 0.35 + 0.18;
    float body = smoothstep(points, points - 0.06, r);
    float core = smoothstep(0.12, 0.0, r) * 0.75;
    return max(body, core);
}

void main() {
    vec2 uv = texCoord;
    float t = TripTime;
    float intensity = clamp(Intensity, 0.0, 1.0);

    vec4 src = texture(DiffuseSampler, uv);

    float wave1 = sin((uv.y * 28.0) + t * 1.8 + TripSeed * 0.01) * 0.004;
    float wave2 = sin((uv.y * 11.0) - t * 1.1 + uv.x * 4.0) * 0.003;
    vec2 wavedUV = uv + vec2((wave1 + wave2) * intensity, 0.0);

    vec3 wavedColor = texture(DiffuseSampler, wavedUV).rgb;

    float stripePhase = uv.y * 13.0 + sin(t * 0.7) * 0.3;
    float stripe = step(0.5, fract(stripePhase));

    vec3 red = vec3(0.75, 0.08, 0.12);
    vec3 white = vec3(0.98, 0.98, 0.96);
    vec3 stripeColor = mix(white, red, stripe);

    float cantonMask = step(uv.x, 0.42) * step(0.46, uv.y);
    vec3 blue = vec3(0.08, 0.16, 0.48);

    vec3 flagBase = mix(stripeColor, blue, cantonMask);

    float stars = 0.0;
    if (cantonMask > 0.5) {
        vec2 cantonUV = vec2(uv.x / 0.42, (uv.y - 0.46) / 0.54);
        vec2 grid = vec2(8.0, 6.0);
        vec2 cell = floor(cantonUV * grid);
        vec2 local = fract(cantonUV * grid);

        float seed = hash(cell + floor(TripSeed));
        vec2 center = vec2(0.5 + (seed - 0.5) * 0.15, 0.5 + (hash(cell + 2.7) - 0.5) * 0.15);

        float pulse = 0.75 + 0.25 * sin(t * 2.2 + seed * 12.0);
        stars = starMask(local, center, 0.30) * pulse;
    }

    vec3 starColor = vec3(1.0);
    vec3 flagColor = flagBase + starColor * stars * cantonMask * 0.7;
    float showFlag = clamp(ShowFlag, 0.0, 1.0);

    float luma = dot(wavedColor, vec3(0.299, 0.587, 0.114));
    vec3 tonedScene = mix(vec3(luma), wavedColor, 0.65);

    float overlayStrength = 0.25 + 0.55 * intensity;
    vec3 overlayColor = mix(tonedScene, flagColor, showFlag);
    vec3 mixed = mix(tonedScene, overlayColor, overlayStrength);

    float flare = smoothstep(0.55, 1.0, intensity) * (0.5 + 0.5 * sin(t * 3.0 + uv.y * 10.0));
    mixed.r += flare * 0.04;
    mixed.b += flare * 0.03;

    fragColor = vec4(clamp(mixed, 0.0, 1.0), src.a);
}
