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

        // 🧠 Memory Game
        CardView cardMemoryGame = rootView.findViewById(R.id.card_memory_game);
        cardMemoryGame.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MemoryGameActivity.class);
            startActivity(intent);
        });

        // 🍎 Apple Game
        CardView cardAppleGame = rootView.findViewById(R.id.card_apple_game);
        cardAppleGame.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AppleGameActivity.class);
            startActivity(intent);
        });

        // ⏳ Coming Soon
        CardView cardEtcGame = rootView.findViewById(R.id.card_etc_game);
        cardEtcGame.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ComingSoonActivity.class);
            startActivity(intent);
        });

        // 🔢 2048 Game
        CardView card2048 = rootView.findViewById(R.id.card_2048);
        card2048.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), Game2048Activity.class);
            startActivity(intent);
        });

        return rootView;
    }
}