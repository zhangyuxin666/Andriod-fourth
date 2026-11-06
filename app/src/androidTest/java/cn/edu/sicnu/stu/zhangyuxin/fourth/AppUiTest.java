package cn.edu.sicnu.stu.zhangyuxin.fourth;

import android.app.Activity;
import android.content.Intent;
import android.widget.*;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import cn.edu.sicnu.stu.zhangyuxin.fourth.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class AppUiTest {
 private android.app.Instrumentation instrumentation() { return InstrumentationRegistry.getInstrumentation(); }
 private <T extends Activity> T launch(Class<T> cls) {
  Intent intent=new Intent(instrumentation().getTargetContext(),cls).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
  return cls.cast(instrumentation().startActivitySync(intent));
 }
 private void ui(Runnable action) { instrumentation().runOnMainSync(action); instrumentation().waitForIdleSync(); }

 @Test public void profileSaveUpdatesHomeAndCancelDoesNotSave() {
  MainActivity home=launch(MainActivity.class);
  ProfileActivity edit=launch(ProfileActivity.class);
  ui(() -> {
   ((EditText)edit.findViewById(R.id.input_phone)).setText("13800138000");
   ((EditText)edit.findViewById(R.id.input_email)).setText("zhangyuxin@example.com");
   ((RadioButton)edit.findViewById(R.id.female)).setChecked(true);
   edit.findViewById(R.id.save).performClick();
  });
  assertEquals("13800138000",Player.phone);
  ui(() -> assertTrue(((TextView)home.findViewById(R.id.value_phone)).getText().toString().contains("13800138000")));
  ProfileActivity cancel=launch(ProfileActivity.class);
  ui(() -> { ((EditText)cancel.findViewById(R.id.input_phone)).setText("13900139000"); cancel.findViewById(R.id.cancel).performClick(); });
  assertEquals("13800138000",Player.phone);
  ui(home::finish);
 }
 @Test public void requiredFieldsRejectEmptyName() {
  ProfileActivity edit=launch(ProfileActivity.class);
  ui(() -> { ((EditText)edit.findViewById(R.id.input_name)).setText(""); edit.findViewById(R.id.save).performClick();
   assertNotNull(((EditText)edit.findViewById(R.id.input_name)).getError()); assertFalse(edit.isFinishing()); edit.finish(); });
 }
 private void tapCard(GameActivity activity, int position) {
  ui(() -> {
   androidx.recyclerview.widget.RecyclerView board = activity.findViewById(R.id.board);
   board.findViewHolderForAdapterPosition(position).itemView.performClick();
  });
 }

 @Test public void cardInteractionScoreLockResetAndBest() {
  GameActivity activity = launch(GameActivity.class);
  List<Card> cards = Arrays.asList(new Card("♥", "A", false, false),
    new Card("♣", "A", false, false), new Card("♦", "A", false, false));
  ui(() -> activity.setGameForTest(new CardMatchingGame(cards)));
  tapCard(activity, 0);
  tapCard(activity, 1);
  assertEquals(-2, activity.getGameForTest().getScore());
  tapCard(activity, 2);
  assertEquals(13, activity.getGameForTest().getScore());
  assertEquals(3, activity.getGameForTest().getMatchedCount());
  ui(() -> {
   androidx.recyclerview.widget.RecyclerView board = activity.findViewById(R.id.board);
   assertFalse(board.findViewHolderForAdapterPosition(0).itemView.isEnabled());
   assertTrue(Player.best >= 13);
   activity.findViewById(R.id.reset).performClick();
   assertEquals(0, activity.getGameForTest().getScore());
   assertEquals(0, activity.getGameForTest().getMatchedCount());
   activity.findViewById(R.id.exit).performClick();
   assertTrue(activity.isFinishing());
  });
 }

 @Test public void mismatchTurnsThreeCardsBack() {
  GameActivity activity = launch(GameActivity.class);
  List<Card> cards = Arrays.asList(new Card("♥", "A", false, false),
    new Card("♣", "K", false, false), new Card("♦", "Q", false, false),
    new Card("♥", "7", false, false), new Card("♣", "7", false, false),
    new Card("♦", "7", false, false));
  ui(() -> activity.setGameForTest(new CardMatchingGame(cards)));
  tapCard(activity, 0);
  tapCard(activity, 1);
  tapCard(activity, 2);
  assertEquals(-5, activity.getGameForTest().getScore());
  assertEquals(0, activity.getGameForTest().getSelectedCount());
  ui(activity::finish);
 }
}
