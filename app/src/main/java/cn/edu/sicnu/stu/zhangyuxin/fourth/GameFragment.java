package cn.edu.sicnu.stu.zhangyuxin.fourth;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.sicnu.stu.zhangyuxin.fourth.model.Card;
import cn.edu.sicnu.stu.zhangyuxin.fourth.model.CardMatchingGame;

public abstract class GameFragment extends Fragment {
    private CardMatchingGame game;
    private String hint;
    private String filename;

    public static final class TwoCards extends GameFragment {
        @Override int groupSize() { return 2; }
    }

    public static final class ThreeCards extends GameFragment {
        @Override int groupSize() { return 3; }
    }

    abstract int groupSize();

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override public void onViewCreated(View view, Bundle state) {
        int groupSize = groupSize();
        filename = "game-" + groupSize + ".dat";
        game = History.read(getActivity(), filename, CardMatchingGame.class);
        if (game == null) game = new CardMatchingGame(24, groupSize);

        TextView title = view.findViewById(R.id.game_title);
        title.setText(groupSize == 2 ? R.string.two_cards : R.string.three_cards);
        ((TextView) view.findViewById(R.id.game_subtitle)).setText(R.string.game_subtitle);
        RecyclerView board = view.findViewById(R.id.board);
        board.setLayoutManager(new GridLayoutManager(getActivity(),
            getResources().getInteger(R.integer.board_columns)));

        hint = currentHint();
        CardAdapter adapter = new CardAdapter(game, index -> {
            Card card = game.cardAtIndex(index);
            if (card.isMatched() || game.isFinished()) return;
            boolean compare = !card.isChosen() && game.getSelectedCount() == groupSize - 1;
            game.chooseCardAtIndex(index);
            hint = compare && !game.isFinished()
                ? getString(card.isMatched() ? R.string.match_hint : R.string.mismatch_hint)
                : currentHint();
            save();
            render(view);
        });
        board.setAdapter(adapter);
        view.findViewById(R.id.reset).setOnClickListener(button -> {
            game.reset();
            board.scrollToPosition(0);
            hint = currentHint();
            save();
            render(view);
        });
        render(view);
    }

    private String currentHint() {
        if (game.isFinished()) {
            int message = game.getMatchedCount() == game.getCardCount()
                ? R.string.complete : R.string.unmatched_end;
            return getString(message, game.getScore());
        }
        int selected = game.getSelectedCount();
        return selected == 0 ? getString(R.string.initial_hint, game.getGroupSize())
            : getString(R.string.chosen_hint, selected, game.getGroupSize() - selected);
    }

    private void save() {
        if (!History.write(getActivity(), filename, game)) hint = getString(R.string.save_failed);
    }

    private void render(View view) {
        ((TextView) view.findViewById(R.id.score)).setText(getString(R.string.score, game.getScore()));
        ((TextView) view.findViewById(R.id.progress)).setText(getString(
            R.string.progress, game.getMatchedCount(), game.getCardCount()));
        ((TextView) view.findViewById(R.id.hint)).setText(hint);
        ((RecyclerView) view.findViewById(R.id.board)).getAdapter().notifyDataSetChanged();
    }
}
