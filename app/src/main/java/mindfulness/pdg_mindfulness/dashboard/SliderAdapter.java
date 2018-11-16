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

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import mindfulness.pdg_mindfulness.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public int[] slide_images={
            R.drawable.ic_terapy_welcome,
            R.drawable.ic_hearth,
            R.drawable.ic_hearth
    };

    public String[] slide_headers={
        "TERAPÍA BASADA EN MINDFULNESS", "ESTRÉS PERCIBIDO","INFORMACIÓN DE USO DEL CELULAR"
    };


    public String[] slide_descriptions={
            "Mediante el método de la terapía cognitiva basada en mindfulness (MCBT) se más consciente del presente y de tu estado de animo.",
            "Realiza autoevaluaciones para determinar tu nivel de estrés percibido durante la última semana.",
            "El uso reciente de tu teléfono esta relacionado con tu nivel de estrés. Compara estos datos y se más consciente cada día."
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
        //imageView.setImageResource(slide_images[position]);
        Picasso.get().load(slide_images[position]).into(imageView);
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
