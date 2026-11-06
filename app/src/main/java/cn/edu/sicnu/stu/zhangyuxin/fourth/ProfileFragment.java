package cn.edu.sicnu.stu.zhangyuxin.fourth;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.io.Serializable;

public class ProfileFragment extends Fragment {
    private static final String FILE = "profile.dat";
    private static final int[] HOBBIES = {R.id.travel, R.id.reading, R.id.gaming, R.id.shopping};

    private static final class Profile implements Serializable {
        private static final long serialVersionUID = 1L;
        String name, id, phone = "", email = "";
        int gender, hobbies;
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override public void onViewCreated(View view, Bundle state) {
        fill(view);
        view.findViewById(R.id.cancel).setOnClickListener(button -> fill(view));
        view.findViewById(R.id.save).setOnClickListener(button -> save(view));
    }

    private void fill(View view) {
        Profile profile = History.read(getActivity(), FILE, Profile.class);
        if (profile == null) {
            profile = new Profile();
            profile.name = getString(R.string.default_name);
            profile.id = getString(R.string.default_id);
        }
        field(view, R.id.input_name).setText(profile.name);
        field(view, R.id.input_student_id).setText(profile.id);
        field(view, R.id.input_phone).setText(profile.phone);
        field(view, R.id.input_email).setText(profile.email);
        RadioGroup gender = view.findViewById(R.id.gender_group);
        if (profile.gender == 0) gender.clearCheck();
        else gender.check(profile.gender == 1 ? R.id.male : R.id.female);
        for (int i = 0; i < HOBBIES.length; i++) {
            ((CheckBox) view.findViewById(HOBBIES[i])).setChecked((profile.hobbies & (1 << i)) != 0);
        }
    }

    private void save(View view) {
        EditText name = field(view, R.id.input_name);
        EditText id = field(view, R.id.input_student_id);
        EditText phone = field(view, R.id.input_phone);
        EditText email = field(view, R.id.input_email);
        Profile profile = new Profile();
        profile.name = name.getText().toString().trim();
        profile.id = id.getText().toString().trim();
        profile.phone = phone.getText().toString().trim();
        profile.email = email.getText().toString().trim();
        if (profile.name.isEmpty() || profile.id.isEmpty()) {
            (profile.name.isEmpty() ? name : id).setError(getString(R.string.required));
            return;
        }
        if (!profile.phone.isEmpty() && !android.util.Patterns.PHONE.matcher(profile.phone).matches()) {
            phone.setError(getString(R.string.invalid_phone));
            return;
        }
        if (!profile.email.isEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(profile.email).matches()) {
            email.setError(getString(R.string.invalid_email));
            return;
        }
        int gender = ((RadioGroup) view.findViewById(R.id.gender_group)).getCheckedRadioButtonId();
        profile.gender = gender == R.id.male ? 1 : gender == R.id.female ? 2 : 0;
        for (int i = 0; i < HOBBIES.length; i++) {
            if (((CheckBox) view.findViewById(HOBBIES[i])).isChecked()) profile.hobbies |= 1 << i;
        }
        boolean saved = History.write(getActivity(), FILE, profile);
        Toast.makeText(getActivity(), saved ? R.string.saved : R.string.save_failed, Toast.LENGTH_SHORT).show();
    }

    private EditText field(View view, int id) {
        return view.findViewById(id);
    }
}
