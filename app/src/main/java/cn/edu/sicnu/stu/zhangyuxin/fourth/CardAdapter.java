package cn.edu.sicnu.stu.zhangyuxin.fourth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import cn.edu.sicnu.stu.zhangyuxin.fourth.model.Card;
import cn.edu.sicnu.stu.zhangyuxin.fourth.model.CardMatchingGame;

final class CardAdapter extends RecyclerView.Adapter<CardAdapter.Holder> {
    interface OnCardClick { void choose(int position); }

    private final CardMatchingGame game;
    private final OnCardClick listener;

    CardAdapter(CardMatchingGame game, OnCardClick listener) {
        this.game = game;
        this.listener = listener;
    }

    @Override public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView face = (TextView) LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_card, parent, false);
        Holder holder = new Holder(face);
        face.setOnClickListener(v -> {
            int position = holder.getBindingAdapterPosition();
            if (position != RecyclerView.NO_POSITION) listener.choose(position);
        });
        return holder;
    }

    @Override public void onBindViewHolder(Holder holder, int position) {
        TextView face = holder.face;
        Context context = face.getContext();
        Card card = game.cardAtIndex(position);
        boolean visible = card.isChosen() || card.isMatched();
        boolean red = card.toString().startsWith(Card.Companion.getValidSuits()[0])
            || card.toString().startsWith(Card.Companion.getValidSuits()[1]);
        face.setText(visible ? card.toString() : context.getString(R.string.card_back));
        face.setBackgroundResource(card.isMatched() ? R.drawable.card_matched
            : visible ? R.drawable.card_front : R.drawable.card_back);
        face.setTextColor(context.getColor(visible ? (red ? R.color.red : R.color.ink) : R.color.paper));
        face.setEnabled(!card.isMatched());
        face.setContentDescription(visible
            ? context.getString(R.string.card_face, position + 1, card.toString())
            : context.getString(R.string.card_hidden, position + 1));
    }

    @Override public int getItemCount() { return game.getCardCount(); }

    static final class Holder extends RecyclerView.ViewHolder {
        final TextView face;
        Holder(TextView face) { super(face); this.face = face; }
    }
}
