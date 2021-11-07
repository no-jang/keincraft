#version 330 core

in vec3 passTextureCoord;
out vec4 fragColor;

uniform sampler2DArray texture_sampler;

void main()
{
	fragColor = texture(texture_sampler, passTextureCoord);
}