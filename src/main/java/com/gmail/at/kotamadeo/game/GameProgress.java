package com.gmail.at.kotamadeo.game;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@RequiredArgsConstructor
public class GameProgress implements Serializable {
    @Serial
    private static final long serialVersionUID = 6627135725157564418L;
    @NonNull
    private int health;
    @NonNull
    private int weapons;
    @NonNull
    private int lvl;
    @NonNull
    private double distance;
}
