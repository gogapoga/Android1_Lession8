package ru.gb.android1_lession8;

public abstract class Style { //базовый класс для стилей, задав значения этих переменных и
    // переопределив функцию возвращающую название стиля в национальном языке
    // в ParamStyle можно подключить еще один стиль, в других классах ничего менять не надо
    protected int background;
    protected int colors;
    protected int colorsPopupOverlay;
    protected int colorsAppBarOverlay;

    public int getColorsPopupOverlay() {
        return colorsPopupOverlay;
    }

    public int getColorsAppBarOverlay() {
        return colorsAppBarOverlay;
    }

    public int getBackground() {
        return background;
    }

    public int getColors() {
        return colors;
    }

}
