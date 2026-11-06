package cn.edu.sicnu.stu.zhangyuxin.fourth;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.sicnu.stu.zhangyuxin.fourth.model.Card;
import cn.edu.sicnu.stu.zhangyuxin.fourth.model.CardMatchingGame;

public class GameActivity extends Activity {
    private CardMatchingGame game;
    private CardAdapter adapter;
    private RecyclerView board;
    private String hint;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        Player.init(this);
        setContentView(R.layout.activity_game);
        int count = getResources().getInteger(R.integer.card_count);
        CardMatchingGame restored = state == null ? null
            : (CardMatchingGame) state.getSerializable(getString(R.string.key_model));
        boolean resume = restored != null && restored.getCardCount() == count;
        game = resume ? restored : new CardMatchingGame(count);
        hint = resume ? state.getString(getString(R.string.key_hint)) : getString(R.string.initial_hint);
        board = findViewById(R.id.board);
        board.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.board_columns)));
        bindBoard();

        findViewById(R.id.reset).setOnClickListener(v -> {
            game.reset();
            board.scrollToPosition(0);
            hint = getString(R.string.initial_hint);
            render();
        });
        findViewById(R.id.exit).setOnClickListener(v -> finish());
    }

    private void bindBoard() {
        adapter = new CardAdapter(game, this::choose);
        board.setAdapter(adapter);
        render();
    }

    private void choose(int index) {
        Card card = game.cardAtIndex(index);
        if (card.isMatched() || game.isFinished()) return;
        boolean completingGroup = !card.isChosen() && game.getSelectedCount() == CardMatchingGame.GROUP_SIZE - 1;
        game.chooseCardAtIndex(index);

        int selected = game.getSelectedCount();
        if (game.getMatchedCount() == game.getCardCount()) {
            hint = getString(R.string.complete, game.getScore());
        } else if (game.isFinished()) {
            hint = getString(R.string.unmatched_end, game.getScore());
        } else if (completingGroup) {
            hint = getString(card.isMatched() ? R.string.match_hint : R.string.mismatch_hint);
        } else {
            hint = selected == 0 ? getString(R.string.initial_hint)
                : getString(R.string.chosen_hint, selected, CardMatchingGame.GROUP_SIZE - selected);
        }
        Player.best = Math.max(Player.best, game.getScore());
        render();
    }

    private void render() {
        ((TextView) findViewById(R.id.score)).setText(getString(R.string.score, game.getScore()));
        ((TextView) findViewById(R.id.progress)).setText(getString(R.string.progress,
            game.getMatchedCount(), game.getCardCount()));
        ((TextView) findViewById(R.id.hint)).setText(hint);
        adapter.notifyDataSetChanged();
    }

    @Override protected void onSaveInstanceState(Bundle out) {
        out.putSerializable(getString(R.string.key_model), game);
        out.putString(getString(R.string.key_hint), hint);
        super.onSaveInstanceState(out);
    }

    public CardMatchingGame getGameForTest() { return game; }

    public void setGameForTest(CardMatchingGame value) {
        game = value;
        hint = getString(R.string.initial_hint);
        bindBoard();
    }
}
