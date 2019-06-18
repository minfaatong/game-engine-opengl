package com.acs.gameeng.base;

public enum Key
{

    A(0x41),
    B(0x42),
    C(0x43),
    D(0x44),
    E(0x45),
    F(0x46),
    G(0x47),
    H(0x48),
    I(0x49),
    J(0x4A),
    K(0x4B),
    L(0x4C),
    M(0x4D),
    N(0x4E),
    O(0x4F),
    P(0x50),
    Q(0x51),
    R(0x52),
    S(0x53),
    T(0x54),
    U(0x55),
    V(0x56),
    W(0x57),
    X(0x58),
    Y(0x59),
    Z(0x5A),
    UP(265),
    DOWN(264),
    LEFT(263),
    RIGHT(262),
    SPACE(32),
    NONE(0);

  private final int value;

  //  K0, K1, K2, K3, K4, K5, K6, K7, K8, K9,
//  F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12,

//  TAB, SHIFT, CTRL, INS, DEL, HOME, END, PGUP, PGDN,
//  BACK, ESCAPE, RETURN, ENTER, PAUSE, SCROLL,
//  NP0, NP1, NP2, NP3, NP4, NP5, NP6, NP7, NP8, NP9,
//  NP_MUL, NP_DIV, NP_ADD, NP_SUB, NP_DECIMAL;
//
  private Key(int value){
    this.value = value;
  }

  public int getValue(){
    return value;
  }
}
