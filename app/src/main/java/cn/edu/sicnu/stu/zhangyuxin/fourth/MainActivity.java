package cn.edu.sicnu.stu.zhangyuxin.fourth;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.widget.RadioGroup;

public class MainActivity extends Activity {
    @Override protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        RadioGroup tabs = findViewById(R.id.tabs);
        int selected = state == null ? R.id.profile : state.getInt("tab", R.id.profile);
        tabs.check(selected);
        if (getFragmentManager().findFragmentById(R.id.content) == null) show(selected);
        tabs.setOnCheckedChangeListener((group, id) -> show(id));
    }

    private void show(int id) {
        Fragment page = id == R.id.profile ? new ProfileFragment()
            : id == R.id.two_cards ? new GameFragment.TwoCards() : new GameFragment.ThreeCards();
        getFragmentManager().beginTransaction().replace(R.id.content, page).commit();
    }

    @Override protected void onSaveInstanceState(Bundle state) {
        RadioGroup tabs = findViewById(R.id.tabs);
        state.putInt("tab", tabs.getCheckedRadioButtonId());
        super.onSaveInstanceState(state);
    }
}
