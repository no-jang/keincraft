#version 330 org.oreon.core

in vec3 passTextureCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
	fragColor = texture(texture_sampler, passTextureCoord);
}