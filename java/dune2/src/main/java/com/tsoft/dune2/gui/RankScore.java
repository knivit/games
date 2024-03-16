package com.tsoft.dune2.gui;

/** Coupling between score and rank name. */
public class RankScore {

    public int rankString; /*!< StringID of the name of the rank. */
    public int score;      /*!< Score needed to obtain the rank. */

    public RankScore(int rankString, int score) {
        this.rankString = rankString;
        this.score = score;
    }
}
