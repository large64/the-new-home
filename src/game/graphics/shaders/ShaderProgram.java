package game.graphics.shaders;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class ShaderProgram {
    private final static FloatBuffer MATRIX_BUFFER = BufferUtils.createFloatBuffer(16);
    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    ShaderProgram(String vertexFile, String fragmentFile) {
        vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);

        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }

    private static int loadShader(String file, int type) {
        StringBuilder shaderSource = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Could not read file. " + file);
            e.printStackTrace();
            System.exit(-1);
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader \"" + file + "\".");
            System.exit(-1);
        }

        return shaderID;
    }

    protected abstract void getAllUniformLocations();

    protected abstract void bindAttributes();

    int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void cleanUp() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    void bindAttribute(int attribute, String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    void loadInt(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    void load3DVector(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    void load2DVector(int location, Vector2f vector) {
        GL20.glUniform2f(location, vector.x, vector.y);
    }

    void loadBoolean(int location, boolean value) {
        float toLoad = 0;

        if (value) {
            toLoad = 1;
        }
        GL20.glUniform1f(location, toLoad);
    }

    void loadMatrix(int location, Matrix4f matrix) {
        matrix.store(MATRIX_BUFFER);
        MATRIX_BUFFER.flip();

        GL20.glUniformMatrix4(location, false, MATRIX_BUFFER);
    }
}
