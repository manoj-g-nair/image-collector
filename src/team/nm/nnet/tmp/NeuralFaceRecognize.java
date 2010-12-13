package team.nm.nnet.tmp;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import team.nm.nnet.util.IOUtils;
import team.nm.nnet.util.LogicUtils;

public class NeuralFaceRecognize implements Runnable{

	private final int FACE_WIDTH = 20;
	
	private final int FACE_HEIGHT = 30;
	
	/**
	 * Luu duong dan de luu file weight.
	 */
	private String strFilename = "";
	
	/// <summary>
    /// So luong input
    /// </summary>
    private final int CintNumberOfInput = 20 * 30; // 30 X 20;

    /// <summary>
    /// So luong output
    /// </summary>
    private int numberOfOutput = 20; //

    /// <summary>
    /// So luong neural o layer an
    /// </summary>
    private final int CintNumberOfHiddenNeural = 800;

    /// <summary>
    /// Luu gi tri bia
    /// </summary>
    private final int CintBias = 30;

    /// <summary>
    /// So luong layer cua mang neural
    /// </summary>
    private final int CintNuberOflayers = 3;

    /// <summary>
    /// Gia tri do doc
    /// </summary>
    private final float slope = 0.014F;

    /// <summary>
    /// Muc do learning
    /// </summary>
    private final float CflLearningRate = 150F;

    /// <summary>
    /// So lan hoc
    /// </summary>
    private final int CintEpochs = 800;

    /// <summary>
    /// Nguong sai cho phep
    /// </summary>
    private final float CflErrorThreshold = 0.0002F;

    /// <summary>
    /// Mang luu tinh hieu nhap vao neural
    /// </summary>
    private float[][] pintInputSignal;

    /// <summary>
    /// Luu gia tri weight cua cac neural
    /// </summary>
    private float[][][] pflWeight = new float[CintNuberOflayers][CintNumberOfHiddenNeural + 100][CintNumberOfHiddenNeural + 100];

    /// <summary>
    /// Luu so luong neural tren tung layer
    /// </summary>
    private int[] pintNeural;

    /// <summary>
    /// Mang luu cac tinh hieu mong muon xuat ra
    /// </summary>
    private int[][] pintDesireOutput;

    /// <summary>
    /// Bien dung de luu gia tri xuat ra cua cac neural trong trong tung layer
    /// </summary>
    private float[][] pflOutputNode = new float[CintNuberOflayers][CintNumberOfHiddenNeural + 100];

    /// <summary>
    /// Luu gia tri nhap vao hien tai trong bo gia tri nhap vao cac phan tu
    /// </summary>
    private float[] pintCurInput;

    /// <summary>
    /// Luu gia tri muon xuat ra hien tai trong bo gia tri muon xuat ra cua cac phan tu
    /// </summary>
    private int[] pintCurDesireOutput;

    /// <summary>
    /// Mang luu gia tri sai khac giua ket qua xuat ra voi ket qua mong muon xuat ra
    /// </summary>
    private float[][] pflError = new float[CintNuberOflayers][CintNumberOfHiddenNeural + 100];
    
    /**
     * Luu tinh hieu nhap vao
     */
    private List<float[]> lstListInput = new ArrayList<float[]>();
    
    /**
     * Luu gia tri mong muon xuat ra
     */
    private List<int[]> lstListDesireOutput = new ArrayList<int[]>();

    /// <summary>
    /// Khoi tao ramdom cho viec hoc cac ki tu
    /// </summary>
    private Random rnd = new Random();

    /**
     * Khoi tao mang neural chi dinh so phan tu output
     * @param numberOfOutPut So phan tu output
     */
    private void psubInitNeural(int numberOfOutPut)
    {
        pintNeural = new int[CintNuberOflayers];
        pintNeural[0] = CintNumberOfInput;
        pintNeural[1] = CintNumberOfHiddenNeural;
        pintNeural[2] = numberOfOutPut;
    }

    private void psubInitWeight()
    {
        Random rnd = new Random();
        for (int i = 1; i < CintNuberOflayers; i++)
        {
            for (int j = 0; j < pintNeural[i]; j++)
            {
                for (int k = 0; k < pintNeural[i - 1]; k++)
                {
                    int intRan = rnd.nextInt(CintBias*2) - CintBias;
                    pflWeight[i][j][k] = intRan;
                   // pflWeight[0, 0, 0] = intRan;
                }
            }
        }

    }
    
    public NeuralFaceRecognize(String strFilename) {
		// TODO Auto-generated constructor stub
    	this.strFilename = strFilename;
    	//psubInitNeural();
    	//psubInitWeight();
    }
    
   
    /**
     * Them du lieu vao bo train
     * @param image Anh can dua vao bo train
     * @param numberOfOutput So luong output
     * @param index Vi tri cua anh
     */
    public void addData(BufferedImage image,int numberOfOutput,int index) {
    	image = ImageProcess.resize(image, FACE_WIDTH, FACE_HEIGHT);
    	float[] input = ImageProcess.imageToArray(image);
    	input = ImageProcess.adaptArray(input);
    	lstListInput.add(input);
    	int[] intArrDesireOutput = new int[numberOfOutput];
      	intArrDesireOutput[index] = 1;
    	lstListDesireOutput.add(intArrDesireOutput);
    }
    
    private float pfncGetTangen(float flActiveValue)
    {
        //return (float)(-1 + (2 / (1 + Math.Exp(-2 * flActiveValue))));
        //float flResult = (float)((Math.Exp(2*flActiveValue) - 1) / (Math.Exp(2*flActiveValue) + 1));
        //return flResult;
         float result = (float)((2 / (1 + Math.exp(-1 * 0.014F * flActiveValue))) - 1);		//Bipolar			
        return result;
    }
    
    private float pfncGetDerivative(float fx)
    {
        float flResult = (float)(0.5F * (1 - Math.pow(fx, 2)));
        return flResult;
    }
    
    private float psubGetAvgError()
    {
        float result = 0.0F;
        for (int i = 0; i < numberOfOutput; i++)
        {
            result = result + pflError[CintNuberOflayers - 1][i];
        }
        result = result / numberOfOutput;
        result = Math.abs(result);
        return result;
    }
    
    private void psubCalOutput()
    {
        float flActiveValue = 0.0F;
        int intNumOfWeigh = 0;
        for (int i = 0; i < CintNuberOflayers; i++)
        {   //Duyet tung layer
            for (int j = 0; j < pintNeural[i]; j++)
            {  
                //Duyet tung neural trong layer
                flActiveValue = 0.0F;
                if (i == 0)
                {
                    intNumOfWeigh = 1;
                }
                else
                {
                    intNumOfWeigh = pintNeural[i - 1];
                }
                for (int k = 0; k < intNumOfWeigh; k++)
                {
                    if (i == 0)
                    {
                        flActiveValue = pintCurInput[j];
                    }
                    else
                    {
                        flActiveValue = flActiveValue + pflOutputNode[i - 1][k] * pflWeight[i][j][k];
                    }   


                }
                pflOutputNode[i][j] = pfncGetTangen(flActiveValue);

            }
        }
    }
    
    private void psubCalError()
    {
        float sum = 0.0F;
        for (int i = 0; i < numberOfOutput; i++)
        {
            pflError[CintNuberOflayers - 1][i] = (float)((pintCurDesireOutput[i] - pflOutputNode[CintNuberOflayers - 1][i]) * pfncGetDerivative(pflOutputNode[CintNuberOflayers - 1][i]));
        }
        for (int i = CintNuberOflayers - 2; i >= 0; i--)
        {
            for (int j = 0; j < pintNeural[i]; j++)
            {
                sum = 0.0F;
                for (int k = 0; k < pintNeural[i + 1]; k++)
                {
                    sum = sum + pflError[i + 1][k] * pflWeight[i + 1][k][j];

                }
                pflError[i][j] = (float)(pfncGetDerivative(pflOutputNode[i][j]) * sum);
               
            }

        }
    }
    
    private void psubCalWeight()
    {
        for (int i = 1; i < CintNuberOflayers; i++)
        {
            for (int j = 0; j < pintNeural[i]; j++)
            {
                for (int k = 0; k < pintNeural[i - 1]; k++)
                {
                    pflWeight[i][j][k] = (float)(pflWeight[i][j][k] + CflLearningRate * pflOutputNode[i - 1][k] * pflError[i][j]);
                }
            }
        }

    }
    
    /**
     * Xac dinh face hay none face
     * @param image Anh can xac dinh
     * @return Ket qua xac dinh
     * Return true: face
     * Return false: none face
     */
    public boolean gfncGetWinner(BufferedImage image) {
    	image = ImageProcess.resize(image, FACE_WIDTH, FACE_HEIGHT);
    	float[] input = ImageProcess.imageToArray(image);
    	pintCurInput = ImageProcess.adaptArray(input);
    	psubCalOutput();
    	//System.out.println(pflOutputNode[CintNuberOflayers - 1][0] + "," + pflOutputNode[CintNuberOflayers - 1][1]);
    	if(pflOutputNode[CintNuberOflayers - 1][0] > 0.5) {
    		return true;
    	}
    	return false;
    }

    @Override
    public void run() {
    	// TODO Auto-generated method stub
    	psubInitWeight();
    	float avgError = 0.0F;
        for (int epoch = 0; epoch <= CintEpochs; epoch++)
        {
            avgError = 0.0F;
            for (int i = 0; i < lstListDesireOutput.size(); i++)
            {
                int index = rnd.nextInt(lstListDesireOutput.size() - 1);
                pintCurDesireOutput = lstListDesireOutput.get(index);
                pintCurInput = lstListInput.get(index);
                psubCalOutput();
                psubCalError();
                psubCalWeight();
                avgError = avgError + psubGetAvgError();
                
            }
            System.out.println("Epoch: " + epoch + " in " + CintEpochs);
            avgError = avgError / lstListDesireOutput.size();
            System.out.println("Avgerror: " + avgError);
            if (avgError < CflErrorThreshold)
            {
                epoch = CintEpochs + 1;
            }
        }
        System.out.println("Saving weight file in " + strFilename);
        saveWeight(strFilename);
        System.out.println("Xong");
    	
    }
    
    /**
	 * Luu file weight xuong file text
	 * @param filename Duong dan luu file
	 */
	public void saveWeight(String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(filename, false);
			PrintWriter pw = new PrintWriter(fos);
			for (int i = 1; i < CintNuberOflayers; i++) {
				for (int j = 0; j < pintNeural[i]; j++) {
					for (int k = 0; k < pintNeural[i - 1]; k++) {
						pw.println(pflWeight[i][j][k]);
					}
				}
			}
			pw.close();
		} catch (Exception ex) {
			System.out.println("Khong tim thay duong dan luu file");
		}
	}
	
	/**
	 * Load weight len tu file text
	 * @param filename Duong dan den file weight can load
	 */
	public void loadWeight(String filename) {
		try {
			FileReader fr = new FileReader(filename);
			BufferedReader br = new BufferedReader(fr);
			for (int i = 1; i < CintNuberOflayers; i++) {
				for (int j = 0; j < pintNeural[i]; j++) {
					for (int k = 0; k < pintNeural[i - 1]; k++) {
						float value = Float.parseFloat(br.readLine());
						pflWeight[i][j][k] = value;
					}
				}
			}
		}
		catch (Exception ex) {
			System.out.println("File weight bi loi");
		}
	}
	
	/**
	 * Cung cap thu muc con chua cac anh de hoc
	 * @param folderPath Duong dan den thu muc train
	 */
	public void addTrainFolder(String folderPath) {
		List<String> listSubFolder = IOUtils.listSubFolder(folderPath);
		//Chua duong dan thu muc la so va chua anh
		List<String> listNumberFolder = new ArrayList<String>();
		for (int i = 0; i < listSubFolder.size(); i ++) {
			if (LogicUtils.isNumber(listSubFolder.get(i)) 
					&& IOUtils.hasFile(folderPath + "\\" + listSubFolder.get(i))) {
				listNumberFolder.add(listSubFolder.get(i));
			}
		}
		psubInitNeural(listNumberFolder.size());
		int numberOfOutput = listNumberFolder.size() + 1;
		this.numberOfOutput = numberOfOutput;
		for (int i = 0; i < listNumberFolder.size(); i ++) {
			int index = Integer.parseInt(listNumberFolder.get(i));
			List<String> listFile = 
				IOUtils.listFileName(folderPath + "\\" + listNumberFolder.get(i));
			for (int j = 0; j < listFile.size(); j ++) {
				BufferedImage image = 
					ImageProcess.load(folderPath + "\\" + listNumberFolder.get(i) + "\\" + listFile.get(j));
				addData(image, numberOfOutput, index);
			}
		}
		
	}
}