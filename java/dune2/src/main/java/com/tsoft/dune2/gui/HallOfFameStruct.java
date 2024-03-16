package com.tsoft.dune2.gui;

/**
 * Hall Of Fame data struct.
 */
public class HallOfFameStruct {

    public char[] name = new char[6];          /*!< Name of the entry. */
    public int score;                          /*!< Score of the entry. */
    public int rank;                           /*!< Rank of the entry. */
    public int campaignID;                     /*!< Which campaign was reached. */
    public int  houseID;                       /*!< Which house was playing. */
    public int  padding1;                      /*!< Padding bytes. */
    public int padding2;                       /*!< Padding bytes. */
}
