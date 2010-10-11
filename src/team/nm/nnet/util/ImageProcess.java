package team.nm.nnet.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Lớp dùng để xử lý ảnh tạm thời không dùng lớp của chí mừng
 * @author MinhNhat
 *
 */
public class ImageProcess {

	/**
	 * Load ảnh từ file thành buffer image
	 * @param filename Đường dẫn tới file ảnh
	 * @return Kết quả loag được
	 */
	public static BufferedImage load(String filename) {
		File file = new File(filename);
		BufferedImage bufferedImage = null;
		Image image;
		try {
			image = ImageIO.read(file);
			if (image != null) {
				bufferedImage = new BufferedImage(image.getWidth(null),
						image.getHeight(null), BufferedImage.TYPE_INT_RGB);
				bufferedImage.createGraphics().drawImage(image, 0, 0, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufferedImage;
	}
	
	/**
	 * Resize ảnh theo một kích thước 
	 * @param image Ảnh cần resize
	 * @param width Chiều rộng cần resize
	 * @param height Chiều dài cần resize
	 * @return Kết quả resize
	 */
	public static BufferedImage resize(BufferedImage image, int width,
			int height) {
		int type = image.getType() != 0 ? image.getType() : 1;
		BufferedImage resizedImage = new BufferedImage(width, height, type);

		Graphics2D g = resizedImage.createGraphics();
		g.setColor(Color.white);
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();

		return resizedImage;
	}
	
	/**
	 * Chiểu ảnh thành mảng ma tran
	 * @param image Ảnh cần chuyển
	 * @return Kết quả chuyển
	 */
	public static Matrix imageToMatrix(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Raster raster = image.getRaster();
        double[][] r = new double[height][width];
        float[] sample = new float[3];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sample = raster.getPixel(j, i, sample);
                r[i][j] = (sample[0] + sample[1] + sample[2]) / 3;
            }
        }
        Matrix result = new Matrix(r, width, height);
        return result;
    }
	
	/**
	 * Chuyển matran thành mảng 
	 * @param matrix Matran cần chuyển
	 * @return Kết quả chuyển
	 */
	public static double[] matrixToArray(Matrix matrix) {
        double[][] a = matrix.getValue();
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        double[] result = new double[height * width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                result[i * width + j] = a[i][j];
            }
        }
        return result;

    }
	
	/**
	 * Hàm chuẩn hóa gi tri matran phù hợp với mạng neural
	 * @param matrix Matran cần chuẩn hóa
	 * @return Kết quả chuẩn hóa
	 */
	public static Matrix nomalizeMatrix(Matrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		double[][] result = new double[height][width]; 
		for (int i = 0; i < height; i ++) {
			for (int j = 0; j < width; j ++) {
				result[i][j] = matrix.getValue()[i][j] / 255;
			}
		}
		return new Matrix(result, width, height);
	}

}