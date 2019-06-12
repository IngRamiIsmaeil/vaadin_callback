package org.koskos.be;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name = DB_Product_Entity.SELECT_ALL_PRODUCT, query = "SELECT P from DB_Product_Entity P"),
        @NamedQuery(name = DB_Product_Entity.SELECT_ALL_PRODUCT_BY_ID, query = "SELECT P from DB_Product_Entity P where P.id=:id"),
        @NamedQuery(name = DB_Product_Entity.SELECT_COUNT_PRODUCT, query = "SELECT count(p) from DB_Product_Entity p"),
        @NamedQuery(name = DB_Product_Entity.SELECT_COUNT_PRODUCT_BY_ID, query = "SELECT count(p) from DB_Product_Entity p where p.id=:id")
})
@Table(name = "product", schema = "lernen", catalog = "")
public class DB_Product_Entity {
    private int id;
    private String name;
    private Double cost;

    public final static String SELECT_ALL_PRODUCT = "selectAllProduct.query";
    public final static String SELECT_COUNT_PRODUCT = "selectCountProduct.query";
    public final static String SELECT_COUNT_PRODUCT_BY_ID = "selectCountProductById.query";
    public final static String SELECT_ALL_PRODUCT_BY_ID = "selectProductById.query";

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "cost", nullable = true, precision = 0)
    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DB_Product_Entity that = (DB_Product_Entity) o;
        return id == that.id &&
                Objects.equals(name, that.name) &&
                Objects.equals(cost, that.cost);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, cost);
    }
}
