

package adompo.ayyash.behay;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InfoSehat extends Fragment {
    public static InfoSehat newInstance() {
        InfoSehat fragment = new InfoSehat();
        return fragment;
    }
    private FragmentTabHost tabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(getActivity(), getChildFragmentManager(), R.id.frame_layout);

        Bundle arg1 = new Bundle();
        arg1.putInt("Arg for Frag1", 1);
        tabHost.addTab(tabHost.newTabSpec("Tab1").setIndicator("Pelayanan Kesehatan"),
                Map1Fragment.class, arg1);


        Bundle arg2 = new Bundle();
        arg2.putInt("Arg for Frag2", 2);
        tabHost.addTab(tabHost.newTabSpec("Tab2").setIndicator("Tempat Olahraga"),
                Map2Fragment.class, arg2);

        Bundle arg3 = new Bundle();
        arg3.putInt("Arg for Frag2", 3);
        tabHost.addTab(tabHost.newTabSpec("Tab3").setIndicator("Jadwal Olahraga"),
                Map3Fragment.class, arg3);

        return tabHost;
    }
}
