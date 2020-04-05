package privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.help;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.R;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.baseactivity.BaseActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.main.MainActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.settings.SettingsActivity;
import privacyfriendlyshoppinglist.secuso.org.privacyfriendlyshoppinglist.ui.statistics.StatisticsActivity;

/**
 * Created by yonjuni on 17.06.16.
 */
public class HelpActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);
        //getFragmentManager().beginTransaction().replace(android.R.id.content, new HelpFragment()).commit();

        overridePendingTransition(0, 0);

    }

    @Override
    protected int getNavigationDrawerID()
    {
        return R.id.nav_help;
    }

    public static class HelpFragment extends PreferenceFragment
    {

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.help);
        }
    }

}