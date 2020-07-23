package com.neolab.heroesGame.heroes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class WarlordFootman extends Footman implements IWarlord {

    private float improveCoefficient = 0.1f;

    public WarlordFootman(final int hp, final int damage, final float precision, final float armor) {
        super(hp, damage, precision, armor);
    }

    @JsonCreator
    protected WarlordFootman(@JsonProperty("unitId") final int unitId, @JsonProperty("hpDefault") final int hpDefault,
                             @JsonProperty("hpMax") final int hpMax, @JsonProperty("hp") final int hp,
                             @JsonProperty("damageDefault") final int damageDefault,
                             @JsonProperty("damage") final int damage, @JsonProperty("precision") final float precision,
                             @JsonProperty("armor") final float armor,
                             @JsonProperty("armorDefault") final float armorDefault,
                             @JsonProperty("defence") final boolean defence,
                             @JsonProperty("improveCoefficient") final float improveCoefficient) {
        super(unitId, hpDefault, hpMax, hp, damageDefault, damage, precision, armor, armorDefault, defence);
        this.improveCoefficient = improveCoefficient;
    }

    public float getImproveCoefficient() {
        return improveCoefficient;
    }

    @Override
    public int getUnitId() {
        return super.getUnitId();
    }

    public static Hero createInstance() {
        final int hp = 180;
        final int damage = 60;
        final float precision = 0.9f;
        final float armor = 0.15f;
        return new WarlordFootman(hp, damage, precision, armor);
    }

    @Override
    public String getClassName() {
        return "Генерал";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final WarlordFootman that = (WarlordFootman) o;
        return Float.compare(that.improveCoefficient, improveCoefficient) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), improveCoefficient);
    }
}
