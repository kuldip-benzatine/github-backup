package layout;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.sk.buildingmanagement.R;

/**
 * Created by ISKM on 04/19/2016.
 */
public class Other_Functions
{
    Activity mContext;
    public void top_window_color(Activity context)
    {
        mContext=context;
        Window window = mContext.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.setStatusBarColor(mContext.getResources().getColor(R.color.window_color));
        }
    }
}
