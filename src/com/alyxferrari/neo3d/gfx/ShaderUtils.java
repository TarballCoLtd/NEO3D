package com.alyxferrari.neo3d.gfx;
import java.io.*;
import java.nio.file.*;
import static org.lwjgl.opengl.GL46.*;
public class ShaderUtils {
	private ShaderUtils() {}
	public static int createProgram(File vertexPath, File fragmentPath) throws IOException {
		return createProgram(new String(Files.readAllBytes(Paths.get(vertexPath.getAbsolutePath()))), new String(Files.readAllBytes(Paths.get(fragmentPath.getAbsolutePath()))));
	}
	public static int createProgram(String vertex, String fragment) {
		int vertexShd = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShd, vertex);
		glCompileShader(vertexShd);
		int fragmentShd = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShd, fragment);
		glCompileShader(fragmentShd);
		int shader = glCreateProgram();
		glAttachShader(shader, vertexShd);
		glAttachShader(shader, fragmentShd);
		glLinkProgram(shader);
		if (glGetProgrami(shader, GL_LINK_STATUS) != GL_TRUE) {
			System.err.println("Shader compilation error:");
			System.err.println(glGetProgramInfoLog(shader, glGetProgrami(shader, GL_INFO_LOG_LENGTH)));
		}
		glDeleteShader(vertexShd);
		glDeleteShader(fragmentShd);
		return shader;
	}
}
