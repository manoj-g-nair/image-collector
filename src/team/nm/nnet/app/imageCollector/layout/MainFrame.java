package team.nm.nnet.app.imageCollector.layout;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Required;

import team.nm.nnet.app.imageCollector.utils.ColorDetection;
import team.nm.nnet.app.imageCollector.utils.ImageFilter;
import team.nm.nnet.app.imageCollector.utils.ImagePreviewPanel;
import team.nm.nnet.core.Const;
import team.nm.nnet.util.ImageUtils;
import team.nm.nnet.util.Matrix;


public class MainFrame extends javax.swing.JFrame {
	
	private static final long serialVersionUID = -5480005990507067644L;
	private Capture capture;
	private Image showingImage;
	
	public MainFrame() {
        setTitle("Dò Tìm và Nhận Dạng Khuôn Mặt - NM Team");
        initComponents();
    }
	
	public void displayImage(Image image) {
		BufferedImage bufferedImage = ImageUtils.toBufferedImage(image);
		if(bufferedImage == null) {
			JOptionPane.showMessageDialog(this, "Không thể hiển thị ảnh này!");
		} else {
			showingImage = fitView(bufferedImage, lblImgView.getWidth(), lblImgView.getHeight());
			lblImgView.setIcon(new javax.swing.ImageIcon(showingImage));
			lblImgName.setText("Camera Image");
			lblImgSize.setText(bufferedImage.getWidth(null) + " x " + bufferedImage.getHeight(null) + " pixels");
			lblImgCap.setText("? KB");
			
			detectFaceCandidates();
		}
	}
	
	public void addFaceCandidates(FacePanel facePanel) {
        pnlFaces.add(facePanel);
        pnlFaces.updateUI();
	}
	
	public void addFacesDemo() {
		FacePanel fp1 = new FacePanel(pnlFaces, ImageUtils.toImage(ImageUtils.load("D:\\FindMyPic\\M.jpg")));
		addFaceCandidates(fp1);
		FacePanel fp2 = new FacePanel(pnlFaces, ImageUtils.toImage(ImageUtils.load("D:\\FindMyPic\\N.jpg")));
		addFaceCandidates(fp2);
		FacePanel fp3 = new FacePanel(pnlFaces, ImageUtils.toImage(ImageUtils.load("D:\\FindMyPic\\Master.jpg")));
		addFaceCandidates(fp3);
	}

    // construct form view
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        splFaces = new javax.swing.JScrollPane();
        pnlFaces = new javax.swing.JPanel();
        btnSearchInDB = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        lblImgView = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnSysFile = new javax.swing.JButton();
        btnWebcam = new javax.swing.JButton();
        lblImgName = new javax.swing.JLabel();
        lblImgSize = new javax.swing.JLabel();
        lblImgCap = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        smnOpen_Image = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        smnDB_New = new javax.swing.JMenuItem();
        smnDB_Load = new javax.swing.JMenuItem();
        smnDB_Save = new javax.swing.JCheckBoxMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(950, 700));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Các khuôn mặt được tìm thấy");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 0);
        jPanel5.add(jLabel3, gridBagConstraints);

        pnlFaces.setBackground(new java.awt.Color(255, 255, 255));
        pnlFaces.setLayout(new javax.swing.BoxLayout(pnlFaces, javax.swing.BoxLayout.PAGE_AXIS));

        addFacesDemo();

        splFaces.setViewportView(pnlFaces);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 136;
        gridBagConstraints.ipady = 515;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(11, 10, 0, 0);
        jPanel5.add(splFaces, gridBagConstraints);

        btnSearchInDB.setText("Tìm ảnh trong CSDL");
        btnSearchInDB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchInDBActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(16, 20, 0, 0);
        jPanel5.add(btnSearchInDB, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(11, 6, 0, 0);
        getContentPane().add(jPanel5, gridBagConstraints);

        lblImgView.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblImgView.setIcon(new javax.swing.ImageIcon("D:\\FindMyPic\\IMG_4003.jpg")); // NOI18N
        lblImgView.setBorder(new javax.swing.border.MatteBorder(null));
        lblImgView.setPreferredSize(new java.awt.Dimension(3074, 2306));

        jLabel2.setText("Ảnh mẫu");

        jLabel4.setText("Tập tin:");

        jLabel5.setText("Kích thước:");

        jLabel6.setText("Dung lượng:");

        btnSysFile.setText("Từ hệ thống");
        btnSysFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSysFileActionPerformed(evt);
            }
        });

        btnWebcam.setText("Từ webcam");
        btnWebcam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWebcamActionPerformed(evt);
            }
        });

        lblImgName.setText("filename.ext");

        lblImgSize.setText("w x h pixels");

        lblImgCap.setText("? KB");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(lblImgView, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(130, 130, 130)
                        .addComponent(btnSysFile, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(109, 109, 109)
                        .addComponent(btnWebcam, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblImgName)
                        .addGap(80, 80, 80)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblImgSize)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 296, Short.MAX_VALUE)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblImgCap)
                        .addGap(47, 47, 47))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(btnWebcam)
                    .addComponent(btnSysFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblImgView, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(lblImgName)
                    .addComponent(lblImgSize)
                    .addComponent(jLabel6)
                    .addComponent(lblImgCap))
                .addContainerGap())
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        getContentPane().add(jPanel7, gridBagConstraints);

        jMenu1.setText("Tài Liệu");

        smnOpen_Image.setText("Chọn ảnh mẫu");
        smnOpen_Image.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                smnOpen_ImageActionPerformed(evt);
            }
        });
        jMenu1.add(smnOpen_Image);

        jMenu3.setText("Cơ Sở Dữ Liệu Ảnh");

        smnDB_New.setText("Tạo mới");
        jMenu3.add(smnDB_New);

        smnDB_Load.setText("Nạp từ tệp");
        jMenu3.add(smnDB_Load);

        smnDB_Save.setSelected(true);
        smnDB_Save.setText("Lưu lại");
        jMenu3.add(smnDB_Save);

        jMenu1.add(jMenu3);

        jMenuBar1.add(jMenu1);

        jMenu5.setText("Tùy Chọn");

        jMenuItem7.setText("Đang Cập Nhật");
        jMenu5.add(jMenuItem7);

        jMenuBar1.add(jMenu5);

        jMenuItem4.setText("Đang xây dựng");
        jMenu2.add(jMenuItem4);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Giới Thiệu");

        jMenuItem5.setText("Chương trình");
        jMenu4.add(jMenuItem5);

        jMenuItem6.setText("Nhóm NM");
        jMenu4.add(jMenuItem6);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>

    private void btnSearchInDBActionPerformed(java.awt.event.ActionEvent evt) {                                              
        /*frmResult frm = new frmResult();
        frm.setTitle("Kết Quả Tìm Kiếm");
        frm.setVisible(true);*/
    }                                             

    private void smnOpen_ImageActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void btnSysFileActionPerformed(java.awt.event.ActionEvent evt) {                                           
    	final JFileChooser chooser = new JFileChooser(".");
		ImageFilter imageFilter = new ImageFilter();
		chooser.addChoosableFileFilter(imageFilter);
		ImagePreviewPanel preview = new ImagePreviewPanel();
		chooser.setAccessory(preview);
		chooser.addPropertyChangeListener(preview);
		chooser.setName("Lấy ảnh mẫu");
		int returnVal = chooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			if (imageFilter.accept(selectedFile)) {
				BufferedImage bufferedImage = ImageUtils.load(selectedFile.getPath());
				if(bufferedImage == null) {
					JOptionPane.showMessageDialog(this, "Không thể mở têp '" + selectedFile.getPath() + "'");
				} else {
					showingImage = fitView(bufferedImage, lblImgView.getWidth(), lblImgView.getHeight());
					lblImgView.setIcon(new javax.swing.ImageIcon(showingImage));
					lblImgName.setText(selectedFile.getName());
					lblImgSize.setText(bufferedImage.getWidth(null) + "x" + bufferedImage.getHeight(null) + " pixels");
					lblImgCap.setText(selectedFile.length() / 1024 + " KB");
					
					detectFaceCandidates();
				}
			}
		}
    }

    private void btnWebcamActionPerformed(java.awt.event.ActionEvent evt) {
    	capture.setParent(this);
        capture.show();
    }
    
    private Image fitView(BufferedImage bufferedImage, int width, int height) {
    	if((bufferedImage.getWidth(null) > width) || (bufferedImage.getHeight(null)) > height) {
    		bufferedImage = ImageUtils.scale(bufferedImage, width, height);
    	}
    	return ImageUtils.toImage(bufferedImage);
    }
    
    private void detectFaceCandidates() {
    	BufferedImage bufferedImage = ImageUtils.toBufferedImage(showingImage);
    	Matrix<Integer> yCrCbArr = ColorDetection.toYCbCr(bufferedImage);
    	for(int i = 0, width = yCrCbArr.getWidth(); i < width; i += Const.JUMP_LENGHT) {
    		if(i + Const.FACE_WIDTH < width) {
	    		BufferedImage subBuff = bufferedImage.getSubimage(i, 0, Const.FACE_WIDTH, Const.FACE_HEIGHT);
	    		FacePanel fp = new FacePanel(pnlFaces, ImageUtils.toImage(subBuff));
	    		addFaceCandidates(fp);
    		}
    	}
    	JOptionPane.showMessageDialog(this, "Change picture!");
    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnSearchInDB;
    private javax.swing.JButton btnSysFile;
    private javax.swing.JButton btnWebcam;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel pnlFaces;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel lblImgCap;
    private javax.swing.JLabel lblImgName;
    private javax.swing.JLabel lblImgSize;
    private javax.swing.JLabel lblImgView;
    private javax.swing.JMenuItem smnDB_Load;
    private javax.swing.JMenuItem smnDB_New;
    private javax.swing.JCheckBoxMenuItem smnDB_Save;
    private javax.swing.JMenuItem smnOpen_Image;
    private javax.swing.JScrollPane splFaces;
    // End of variables declaration

	public Capture getCapture() {
		return capture;
	}

	@Required
	public void setCapture(Capture capture) {
		this.capture = capture;
	}

}