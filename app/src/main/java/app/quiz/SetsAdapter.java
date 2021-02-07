package app.quiz;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

// класс, с помощью которого создаем список уровней
public class SetsAdapter extends BaseAdapter {

    private final int numOfSets;

    public SetsAdapter(int numOfSets) {
        this.numOfSets = numOfSets;
    }

    @Override
    public int getCount() {
        return numOfSets;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_iteam_layout, parent, false);
        } else {
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), Levels.class);
                intent.putExtra("SETNUMBER", position + 1);
                // intent.putExtra("Set", numOfSets);
                parent.getContext().startActivity(intent);

            }
        });

        ((TextView) view.findViewById(R.id.level_number)).setText(String.valueOf(position + 1));
        return view;
    }
}
