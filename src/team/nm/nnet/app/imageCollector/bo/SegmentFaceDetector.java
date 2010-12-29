package team.nm.nnet.app.imageCollector.bo;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import team.nm.nnet.app.imageCollector.layout.FacePanel;
import team.nm.nnet.app.imageCollector.om.ColorSegment;
import team.nm.nnet.app.imageCollector.om.Pixel;
import team.nm.nnet.core.Const;
import team.nm.nnet.tmp.NeuralNetwork;
import team.nm.nnet.util.ImageUtils;

public class SegmentFaceDetector extends Thread {
    
    private volatile boolean state = false;
    private JPanel pnlFaces;
    private JLabel lblProcess;
    private BufferedImage bufferedImage;
    private NeuralNetwork neuralNetwork;
    
    public SegmentFaceDetector(JPanel pnlFaces, JLabel lblProcess, Image image, NeuralNetwork neuralNetwork) {
        this.pnlFaces = pnlFaces;
        this.lblProcess = lblProcess;
        if(image != null) {
        	bufferedImage = ImageUtils.toBufferedImage(image);
        }
        this.neuralNetwork = neuralNetwork;
    }

    public void run() {
        if(bufferedImage == null) {
            return;
        }

        // Mark this thread is running
        state = true;
        lblProcess.setIcon(new ImageIcon(Const.CURRENT_DIRECTORY + Const.RESOURCE_PATH + "waiting.gif"));
        
        findCandidates(bufferedImage);
        
        // Finish detecting
        lblProcess.setIcon(new ImageIcon(Const.CURRENT_DIRECTORY + Const.RESOURCE_PATH + "check.png"));
        state = false;
        System.gc();
    }
    
    public boolean isDetecting() {
        return state;
    }

    public void requestStop() {
        state = false;
    }

    public boolean isCandidate(ColorSegment colorSegment) {
        if(colorSegment.getPixels().size() < Const.MINIMUM_SKIN_PIXEL_THRESHOLD) {
            return false;
        }
        float whiteRatio = (float) colorSegment.getPixels().size() / (colorSegment.getWidth() * colorSegment.getHeight());
        if(whiteRatio < 0.4) {
            return false;
        }

        return true;
    }
    
    protected void findCandidates(BufferedImage bufferedImage) {
        ColorSegmentation colorSegmentation = new ColorSegmentation();
        List<ColorSegment> segments = colorSegmentation.segment(bufferedImage);
        if(segments == null) {
            return;
        }
        for(ColorSegment segment : segments) {
            if(!state) {
                colorSegmentation.requestStop();
                return;
            }
            if (isCandidate(segment)) {
                try{
                    List<ColorSegment> subSegments = separateRegions(segment);
                    if(subSegments == null) {
                        subSegments = new ArrayList<ColorSegment>();
                        subSegments.add(segment);
                    }
                    for(ColorSegment subSegment : subSegments) {
//                      BufferedImage subBuff = extractSingleFace(subSegment);
                        BufferedImage subBuff = bufferedImage.getSubimage(subSegment.getLeft(), subSegment.getBottom(), subSegment.getWidth(), subSegment.getHeight());
                        if(subBuff != null) {
        //                  subBuff = ImageUtils.resize(subBuff, Const.FACE_WIDTH, Const.FACE_HEIGHT);
        //                  if(neuralNetwork.gfncGetWinner(subBuff) > Const.NETWORK_FACE_VALIDATION_THRESHOLD) {
            //                  int x = ((segment.getLeft() - Const.SPAN_FACE_BOX) <= 0) ? segment.getLeft() - Const.SPAN_FACE_BOX : segment.getLeft(); 
            //                  int y = ((segment.getBottom() - Const.SPAN_FACE_BOX) <= 0) ? segment.getBottom() - Const.SPAN_FACE_BOX : segment.getBottom(); 
            //                  int w = ((segment.getWidth() + Const.SPAN_FACE_BOX) <= bufferedImage.getWidth()) ? segment.getWidth() + Const.SPAN_FACE_BOX : segment.getWidth(); 
            //                  int h = ((segment.getHeight() + Const.SPAN_FACE_BOX) <= bufferedImage.getHeight()) ? segment.getHeight() + Const.SPAN_FACE_BOX : segment.getHeight(); 
            //                  
            //                  subBuff = bufferedImage.getSubimage(x, y, w, h);
            //                    subBuff = ImageUtils.resize(subBuff, Const.FACE_WIDTH, Const.FACE_HEIGHT);
                                FacePanel fp = new FacePanel(pnlFaces, ImageUtils.toImage(subBuff));
                                fp.setFaceName((float)segment.getWidth() / segment.getHeight() + " : " + segment.getWidth() + " x " + segment.getHeight());
                                addFaceCandidates(fp);
        //                  }
                        } 
                    }
                }catch(Exception e) {
                    System.out.println(String.format("l: %d, r: %d, b: %d, t:%d, w: %d, h: %d", segment.getLeft(), segment.getRight(), segment.getBottom(), segment.getTop(), segment.getWidth(), segment.getHeight()));
                }
            }
        }
    }
    
    protected List<ColorSegment> separateRegions(ColorSegment segment) {
        List<Pixel> brokenPoints = segment.getBrokenPoints(bufferedImage); 
        if((brokenPoints == null) || (brokenPoints.size() < 1)) {
            return null;
        }
        Collections.sort(brokenPoints);
        
        List<ColorSegment> regions = new ArrayList<ColorSegment>();
        int top = segment.getTop();
        int bottom = segment.getBottom();
        int left = segment.getLeft();
        for(Pixel p : brokenPoints) {
            regions.add(new ColorSegment(left, top, p.getX(), bottom));
            left = p.getX();
        }
        
        // Add the last region
        regions.add(new ColorSegment(left, top, segment.getRight(), bottom));
        return regions;
    }
    
    protected BufferedImage extractSingleFace(ColorSegment segment) {
        int width = segment.getWidth();
        int height = segment.getHeight();
        BufferedImage segmentBuff = bufferedImage.getSubimage(segment.getLeft(), segment.getBottom(), width, height);
        
        float max = 0;
        BufferedImage candidate = null;
        double[] scales = {1, 0.6, 0.5};
        for(double scale : scales){
            int w = (int) (width * scale), h = (int) (height * scale);
            for(int i = 0, ww = width - w; i <= ww; i += Const.JUMP_LENGHT) {
                for(int j = 0, hh = height - h; j <= hh; j += Const.JUMP_LENGHT) {
                    BufferedImage subBuff = segmentBuff.getSubimage(i, j, w, h);
                    subBuff = ImageUtils.resize(subBuff, Const.FACE_WIDTH, Const.FACE_HEIGHT);
//                  FacePanel fp = new FacePanel(pnlFaces, ImageUtils.toImage(subBuff));
//                  fp.setFaceName((float)segment.getWidth() / segment.getHeight() + " : " + segment.getWidth() + " x " + segment.getHeight());
//                  addFaceCandidates(fp);
                    float outVal = neuralNetwork.gfncGetWinner(subBuff);
                    if((outVal > Const.NETWORK_FACE_VALIDATION_THRESHOLD) && (outVal > max)) {
                        max = outVal;
                        candidate = subBuff;
                    }
                }
            }
        }
        return candidate;
    }
    
    protected void addFaceCandidates(FacePanel facePanel) {
        pnlFaces.add(facePanel);
        pnlFaces.updateUI();
    }
}