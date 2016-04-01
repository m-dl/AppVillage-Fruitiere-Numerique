package theron_b.com.visitetablette.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.graphics.Palette;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import theron_b.com.visitetablette.gallery.helper.TouchImageView;

public class BitmapLoader {

    private Bitmap          m_Bitmap;

    private boolean         m_Central;
    private boolean         m_Palette;
    private boolean         m_FourByThree;

    private float           m_ImageHeight;
    private float           m_ImageWidth;

    private String          m_File;
    private String          m_RGB;
    private String          m_TitleTextColor;

    public BitmapLoader(String image) {
        m_File = image;
    }

    public void         setPaletteWithoutImage() {
        m_RGB = null;
        m_Palette = true;
        BitmapWorkerThread task = new BitmapWorkerThread(true);
        task.run();
    }

    public void         setImage(int widht, int height) {
        m_RGB = null;
        BitmapWorkerThread task = new BitmapWorkerThread(widht, height);
        task.run();
    }

    public void         setImageView(ImageView imageView, int widht, int height, boolean centrate, boolean palette) {
        m_Palette = palette;
        m_Central = centrate;
        m_RGB = null;
        BitmapWorkerThread task = new BitmapWorkerThread(imageView, widht, height);
        task.run();
    }

    public void         setImageView(ImageView imageView, int widht, int height, boolean centrate) {
        m_Central = centrate;
        m_RGB = null;
        BitmapWorkerThread task = new BitmapWorkerThread(imageView, widht, height);
        task.run();
    }

    public void         setImageView(ImageView imageView, int widht, int height) {
        m_RGB = null;
        BitmapWorkerThread task = new BitmapWorkerThread(imageView, widht, height);
        task.run();
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String file, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
    }

    public String getRGB() {
        return m_RGB;
    }

    public boolean isImagePhotoSphere() {
        return (m_ImageHeight >= 4000 && m_ImageWidth >= 3000);
    }

    public boolean isImagePanorama() {
        return (m_ImageHeight >= 8000 || m_ImageWidth >= 8000);
    }

    public void setImage(BitmapFactory.Options options) {
        m_RGB = null;
        BitmapWorkerThread task = new BitmapWorkerThread(options);
        task.run();
    }

    public void destroy() {
        if (!m_Bitmap.isRecycled()) {
            m_Bitmap.recycle();
            m_Bitmap = null;
        }
    }

    //todo
    class BitmapWorkerThread extends Thread {
        private final WeakReference<ImageView> imageViewReference;

        private int m_Width;
        private int m_Height;

        private boolean m_PaletteOnly;
        private boolean m_BitmapOnly;
        private boolean m_FullBitmap;

        private BitmapFactory.Options m_Options;

        public BitmapWorkerThread(Boolean m_paletteOnly) {
            imageViewReference = null;
            m_PaletteOnly = m_paletteOnly;
        }

        public BitmapWorkerThread(ImageView imageView, int width, int height) {
            imageViewReference = new WeakReference<>(imageView);
            m_Width = width;
            m_Height = height;
        }

        public BitmapWorkerThread(int width, int height) {
            imageViewReference = null;
            m_BitmapOnly = true;
            m_Width = width;
            m_Height = height;
        }

        public BitmapWorkerThread(BitmapFactory.Options options) {
            imageViewReference = null;
            m_FullBitmap = true;
            m_Options = options;
        }

        public void run() {
            if (m_FullBitmap) {
                m_Bitmap = BitmapFactory.decodeFile(m_File);
            } else {
                final WeakReference<Bitmap> bitmapWeakReference = new WeakReference<>(decodeSampledBitmapFromResource(m_File, m_Width, m_Height));
                if (bitmapWeakReference.get() != null) {
                    if (!m_BitmapOnly) {
                        if (m_PaletteOnly) {
                            setPalette(bitmapWeakReference.get());
                        } else {
                            applyBitmap(bitmapWeakReference.get());
                        }
                    } else {
                        m_Bitmap = bitmapWeakReference.get();
                    }
                }
            }
        }

        private void applyBitmap(Bitmap bitmap) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                setImageDimension();
                if (isImagePhotoSphere() || isImagePanorama()) {
                    ArrayList<Integer> position = getImagePosition(m_Width, m_Height, bitmap.getWidth(), bitmap.getHeight());
                    imageView.setImageBitmap(Bitmap.createBitmap(bitmap,
                            position.get(0),
                            position.get(1),
                            position.get(2),
                            position.get(3)));
                } else {
                    imageView.setImageBitmap(bitmap);
                }
                if (m_Central) {
                    if (m_Palette)
                        setPalette(bitmap);
                    setImageToCenter(imageView, m_Width, m_Height);
                }
            }
        }
    }

    private ArrayList<Integer> getImagePosition(int m_width, int m_height, int ImageWidth, int ImageHeight) {
        ArrayList<Integer> imagePosition = new ArrayList<>();
        imagePosition.add(((centerX() - m_width) % ImageWidth));
        imagePosition.add(((centerY() - m_height) % ImageHeight));
        imagePosition.add(((centerX() + m_width) % ImageWidth));
        imagePosition.add(((centerY() + m_height) % ImageHeight));
        return imagePosition;
    }

    private int centerY() {
        return (int)m_ImageHeight / 2;
    }

    private int centerX() {
        return (int)m_ImageWidth / 2;
    }

    private void setImageToCenter(ImageView imageView, int m_Width, int m_Height) {
        m_FourByThree = m_ImageWidth / m_ImageHeight < 1.35;
        if (imageView.getLayoutParams().toString().contains("FrameLayout")) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(m_Width, m_Height);
            layoutParams.gravity = Gravity.CENTER;
            if (!m_FourByThree) {
                layoutParams.setMargins(0, 0, 100, 0);
            }
            imageView.setLayoutParams(layoutParams);
        }
    }

    private void setPalette(Bitmap bitmap) {
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                if (palette != null) {
                    List<Palette.Swatch> vibrant = palette.getSwatches();
                    MyColor myColor = new MyColor();
                    int RGB = myColor.getMostUsedColor(vibrant);
                    if (RGB != 0) {
                        m_RGB = myColor.getHexadecimal(RGB);
                    }
                    m_TitleTextColor = myColor.getTitleColor();
                }
            }
        });
    }

    public void setImageDimension() {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(m_File, options);
        m_ImageHeight = options.outHeight;
        m_ImageWidth = options.outWidth;
        options.inJustDecodeBounds = false;
    }

    public String getM_TitleTextColor() {
        return m_TitleTextColor;
    }

    public boolean isM_FourByThree() {
        return m_FourByThree;
    }

    public Bitmap getM_Bitmap() {
        return m_Bitmap;
    }

}
