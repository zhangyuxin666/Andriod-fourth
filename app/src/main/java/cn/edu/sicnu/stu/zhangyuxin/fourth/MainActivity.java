package cn.edu.sicnu.stu.zhangyuxin.fourth;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;
public class MainActivity extends Activity {
 @Override public void onCreate(Bundle state) { super.onCreate(state); Player.init(this); setContentView(R.layout.activity_main);
  findViewById(R.id.edit).setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
  findViewById(R.id.play).setOnClickListener(v -> startActivity(new Intent(this, GameActivity.class)));
 }
 @Override public boolean onCreateOptionsMenu(android.view.Menu menu) {
  getMenuInflater().inflate(R.menu.home, menu);
  return true;
 }
 @Override public boolean onOptionsItemSelected(android.view.MenuItem item) {
  if (item.getItemId() != R.id.about) return super.onOptionsItemSelected(item);
  new android.app.AlertDialog.Builder(this)
    .setTitle(R.string.about)
    .setMessage(R.string.about_message)
    .setPositiveButton(android.R.string.ok, null)
    .show();
  return true;
 }
 @Override protected void onResume() { super.onResume();
  bind(R.id.value_name,R.string.name,Player.name); bind(R.id.value_gender,R.string.gender,Player.gender);
  bind(R.id.value_student_id,R.string.student_id,Player.id); bind(R.id.value_phone,R.string.phone,Player.phone);
  bind(R.id.value_email,R.string.email,Player.email); bind(R.id.value_hobbies,R.string.hobbies,Player.hobbies);
  ((TextView)findViewById(R.id.best)).setText(getString(R.string.best_score,Player.best));
 }
 private void bind(int view,int label,String value) { ((TextView)findViewById(view)).setText(getString(R.string.field_value,getString(label),value)); }
}
