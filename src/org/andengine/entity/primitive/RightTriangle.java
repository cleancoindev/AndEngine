package org.andengine.entity.primitive;

import android.opengl.GLES20;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.RectangularShape;
import org.andengine.opengl.shader.PositionColorShaderProgram;
import org.andengine.opengl.shader.constants.ShaderProgramConstants;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.HighPerformanceVertexBufferObject;
import org.andengine.opengl.vbo.IVertexBufferObject;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributes;
import org.andengine.opengl.vbo.attribute.VertexBufferObjectAttributesBuilder;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 12:18:49 - 13.03.2010
 */
public class RightTriangle extends RectangularShape {
    // ===========================================================
	// Constants
	// ===========================================================

    public enum TriangleType {
        LEFT,
        RIGHT
    }

    public static final int VERTEX_INDEX_X = 0;
	public static final int VERTEX_INDEX_Y = RightTriangle.VERTEX_INDEX_X + 1;
	public static final int COLOR_INDEX = RightTriangle.VERTEX_INDEX_Y + 1;

	public static final int VERTEX_SIZE = 2 + 1;
	public static final int VERTICES_PER_TRIANGLE = 3;
	public static final int TRIANGLE_SIZE = RightTriangle.VERTEX_SIZE * RightTriangle.VERTICES_PER_TRIANGLE;

	public static final VertexBufferObjectAttributes VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT = new VertexBufferObjectAttributesBuilder(2)
		.add(ShaderProgramConstants.ATTRIBUTE_POSITION_LOCATION, ShaderProgramConstants.ATTRIBUTE_POSITION, 2, GLES20.GL_FLOAT, false)
		.add(ShaderProgramConstants.ATTRIBUTE_COLOR_LOCATION, ShaderProgramConstants.ATTRIBUTE_COLOR, 4, GLES20.GL_UNSIGNED_BYTE, true)
		.build();

	// ===========================================================
	// Fields
	// ===========================================================

	protected final IRectangleVertexBufferObject vertexBufferObject;
    private TriangleType type;

	// ===========================================================
	// Constructors
	// ===========================================================

	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} in {@link org.andengine.opengl.vbo.VertexBufferObject.DrawType#STATIC} with the {@link org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute}s: {@link RightTriangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public RightTriangle(final float pX, final float pY, final float pWidth, final float pHeight, final VertexBufferObjectManager pVertexBufferObjectManager, TriangleType type) {
		this(pX, pY, pWidth, pHeight, pVertexBufferObjectManager, DrawType.STATIC, type);
	}

	/**
	 * Uses a default {@link HighPerformanceRectangleVertexBufferObject} with the {@link org.andengine.opengl.vbo.attribute.VertexBufferObjectAttribute}s: {@link RightTriangle#VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT}.
	 */
	public RightTriangle(final float pX, final float pY, final float pWidth, final float pHeight, final VertexBufferObjectManager pVertexBufferObjectManager, final DrawType pDrawType, TriangleType type) {
		this(pX, pY, pWidth, pHeight, new HighPerformanceRectangleVertexBufferObject(pVertexBufferObjectManager, RightTriangle.TRIANGLE_SIZE, pDrawType, true, RightTriangle.VERTEXBUFFEROBJECTATTRIBUTES_DEFAULT, type), type);
	}

	public RightTriangle(final float pX, final float pY, final float pWidth, final float pHeight, final IRectangleVertexBufferObject pRectangleVertexBufferObject, TriangleType type) {
		super(pX, pY, pWidth, pHeight, PositionColorShaderProgram.getInstance());

		this.vertexBufferObject = pRectangleVertexBufferObject;
        this.type = type;

		this.onUpdateVertices();
		this.onUpdateColor();

		this.setBlendingEnabled(true);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================


    @Override public float[] getSceneCenterCoordinates() {
        float x = 2 * this.mWidth / 3;
        if (type == TriangleType.RIGHT) {
            x = this.mWidth / 3;
        }
        return this.convertLocalToSceneCoordinates(x, 2 * this.mHeight / 3);
    }

    @Override
	public IRectangleVertexBufferObject getVertexBufferObject() {
		return this.vertexBufferObject;
	}

	@Override
	protected void preDraw(final GLState pGLState, final Camera pCamera) {
		super.preDraw(pGLState, pCamera);

		this.vertexBufferObject.bind(pGLState, this.mShaderProgram);
	}

	@Override
	protected void draw(final GLState pGLState, final Camera pCamera) {
		this.vertexBufferObject.draw(GLES20.GL_TRIANGLE_STRIP, RightTriangle.VERTICES_PER_TRIANGLE);
	}

	@Override
	protected void postDraw(final GLState pGLState, final Camera pCamera) {
		this.vertexBufferObject.unbind(pGLState, this.mShaderProgram);

		super.postDraw(pGLState, pCamera);
	}

	@Override
	protected void onUpdateColor() {
		this.vertexBufferObject.onUpdateColor(this);
	}

	@Override
	protected void onUpdateVertices() {
		this.vertexBufferObject.onUpdateVertices(this);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface IRectangleVertexBufferObject extends IVertexBufferObject {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		public void onUpdateColor(final RightTriangle pRectangle);
		public void onUpdateVertices(final RightTriangle pRectangle);
	}

	public static class HighPerformanceRectangleVertexBufferObject extends HighPerformanceVertexBufferObject implements IRectangleVertexBufferObject {
        private final TriangleType type;
        // ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		// ===========================================================
		// Constructors
		// ===========================================================

		public HighPerformanceRectangleVertexBufferObject(final VertexBufferObjectManager pVertexBufferObjectManager,
                                                          final int pCapacity, final DrawType pDrawType, final boolean pAutoDispose, final VertexBufferObjectAttributes pVertexBufferObjectAttributes, TriangleType type) {
			super(pVertexBufferObjectManager, pCapacity, pDrawType, pAutoDispose, pVertexBufferObjectAttributes);
            this.type = type;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		@Override
		public void onUpdateColor(final RightTriangle pRectangle) {
			final float[] bufferData = this.mBufferData;

			final float packedColor = pRectangle.getColor().getABGRPackedFloat();

			bufferData[0 * RightTriangle.VERTEX_SIZE + RightTriangle.COLOR_INDEX] = packedColor;
			bufferData[1 * RightTriangle.VERTEX_SIZE + RightTriangle.COLOR_INDEX] = packedColor;
			bufferData[2 * RightTriangle.VERTEX_SIZE + RightTriangle.COLOR_INDEX] = packedColor;
//			bufferData[3 * RightTriangle.VERTEX_SIZE + RightTriangle.COLOR_INDEX] = packedColor;

			this.setDirtyOnHardware();
		}

		@Override
		public void onUpdateVertices(final RightTriangle pTriangle) {
			final float[] bufferData = this.mBufferData;

			final float x = 0;
			final float y = 0;
			final float x2 = pTriangle.getWidth(); // TODO Optimize with field access?
			final float y2 = pTriangle.getHeight(); // TODO Optimize with field access?

            if (type == TriangleType.LEFT) {
			    bufferData[0 * RightTriangle.VERTEX_SIZE + RightTriangle.VERTEX_INDEX_X] = x;
            } else {
                bufferData[0 * RightTriangle.VERTEX_SIZE + RightTriangle.VERTEX_INDEX_X] = x2;
            }
			bufferData[0 * RightTriangle.VERTEX_SIZE + RightTriangle.VERTEX_INDEX_Y] = y;

			bufferData[1 * RightTriangle.VERTEX_SIZE + RightTriangle.VERTEX_INDEX_X] = x;
			bufferData[1 * RightTriangle.VERTEX_SIZE + RightTriangle.VERTEX_INDEX_Y] = y2;

			bufferData[2 * RightTriangle.VERTEX_SIZE + RightTriangle.VERTEX_INDEX_X] = x2;
			bufferData[2 * RightTriangle.VERTEX_SIZE + RightTriangle.VERTEX_INDEX_Y] = y2;

//			bufferData[3 * RightTriangle.VERTEX_SIZE + RightTriangle.VERTEX_INDEX_X] = x2;
//			bufferData[3 * RightTriangle.VERTEX_SIZE + RightTriangle.VERTEX_INDEX_Y] = y2;

			this.setDirtyOnHardware();
		}

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}

}
