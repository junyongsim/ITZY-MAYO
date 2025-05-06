package com.syu.itzy_mayo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class FeedFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_feed, container, false);
        // 🧠 Memory Game 카드 클릭 이벤트 추가
        CardView cardMemoryGame = rootView.findViewById(R.id.card_memory_game);
        cardMemoryGame.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MemoryGameActivity.class);
            startActivity(intent);
        });
        return rootView;
    }
}
