package com.neolab.heroesGame.heroes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Archer extends Hero {

    public Archer(int hp, int damage, float precision, float armor) {
        super(hp, damage, precision, armor);
    }

    @JsonCreator
    public Archer(@JsonProperty("hpDefault") final int hpDefault, @JsonProperty("hpMax") final int hpMax,
                  @JsonProperty("hp") final int hp, @JsonProperty("damageDefault") final int damageDefault,
                  @JsonProperty("damage") final int damage, @JsonProperty("precision") final float precision,
                  @JsonProperty("armor") final float armor, @JsonProperty("armorDefault") final float armorDefault,
                  @JsonProperty("defence") final boolean defence) {
        super(hpDefault, hpMax, hp, damageDefault, damage, precision, armor, armorDefault, defence);
    }
}