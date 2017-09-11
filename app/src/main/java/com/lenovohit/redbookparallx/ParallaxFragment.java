package com.lenovohit.redbookparallx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ParallaxFragment extends Fragment {
	private List<View> views = new ArrayList<>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		int layoutId = bundle.getInt("layoutId");
		ParallaxLayoutInflater layoutInflater = new ParallaxLayoutInflater(inflater, getActivity(),this);
		return layoutInflater.inflate(layoutId, null);
	}

	public List<View> getViews() {
		return views;
	}
}
