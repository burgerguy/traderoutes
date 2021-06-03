package org.mariemontschools.ryang.traderoutes.data;

import java.util.List;

public class YearData {
    private final List<ShipData> ships;
    // TODO: add more data, maybe trade route names and contents? maybe map ids for different border changes or name changes?

    public YearData(List<ShipData> ships) {
        this.ships = ships;
        //Collections.shuffle(this.ships);
    }

    public List<ShipData> getShips() {
        return ships;
    }
}
