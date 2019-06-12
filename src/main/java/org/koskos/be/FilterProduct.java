package org.koskos.be;

import java.io.Serializable;
import java.util.Objects;

public class FilterProduct implements Serializable{
    private int filterById;
    private float filtyByCost;
    private String filterByName;

    public FilterProduct(int filterById, float filtyByCost, String filterByName) {
        this.filterById = filterById;
        this.filtyByCost = filtyByCost;
        this.filterByName = filterByName;
    }

    public FilterProduct() {
    }

    public int getFilterById() {
        return filterById;
    }

    public float getFiltyByCost() {
        return filtyByCost;
    }

    public String getFilterByName() {
        return filterByName;
    }

    public void setFilterById(int filterById) {
        this.filterById = filterById;
    }

    public void setFiltyByCost(float filtyByCost) {
        this.filtyByCost = filtyByCost;
    }

    public void setFilterByName(String filterByName) {
        this.filterByName = filterByName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FilterProduct)) return false;
        FilterProduct that = (FilterProduct) o;
        return getFilterById() == that.getFilterById() &&
                Float.compare(that.getFiltyByCost(), getFiltyByCost()) == 0 &&
                Objects.equals(getFilterByName(), that.getFilterByName());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getFilterById(), getFiltyByCost(), getFilterByName());
    }

    @Override
    public String toString() {
        return "FilterProduct{" +
                "filterById=" + filterById +
                ", filtyByCost=" + filtyByCost +
                ", filterByName='" + filterByName + '\'' +
                '}';
    }
}
