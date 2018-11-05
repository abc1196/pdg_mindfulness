package mindfulness.pdg_mindfulness.dashboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import mindfulness.pdg_mindfulness.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public int[] slide_images={
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
    };

    public String[] slide_headers={
        "HOME", "HRV","PROFILE"
    };


    public String[] slide_descriptions={
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas vel tincidunt nulla. Etiam commodo, sem eu cursus pharetra, quam nisl.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas vel tincidunt nulla. Etiam commodo, sem eu cursus pharetra, quam nisl.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas vel tincidunt nulla. Etiam commodo, sem eu cursus pharetra, quam nisl."
    };

    public SliderAdapter(Context context){
        this.context=context;
    }
    @Override
    public int getCount() {
        return slide_headers.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view ==(RelativeLayout)o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout, container,false);
        ImageView imageView =(ImageView)view.findViewById(R.id.imageView);
        TextView textTitleView=(TextView)view.findViewById(R.id.textTitle);
        TextView textDescriptionView=(TextView)view.findViewById(R.id.textDescription);
        imageView.setImageResource(slide_images[position]);
        textTitleView.setText(slide_headers[position]);
        textDescriptionView.setText(slide_descriptions[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((RelativeLayout)object);
    }
}