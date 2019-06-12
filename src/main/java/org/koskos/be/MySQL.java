package org.koskos.be;

import java.io.Serializable;
import java.sql.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MySQL implements Serializable{
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public MySQL() {
        try {
            final Class<?> aClass = Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/lernen?user=root&password=secret");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select CURRENT_DATE");
            while(resultSet.next())
                System.out.println(resultSet.getString(1));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(null != statement) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(null != resultSet){
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Product> getProductFromUntil(int limit, int offset, FilterProduct filterProduct){
        List<Product> productList = new LinkedList<>();
        try {
            if(null == filterProduct){
                preparedStatement = connection.prepareStatement("select * from lernen.product order by id limit ?,? ");
                // in ms sql will be
                //SELECT * from vgr.dbo.CareUnits order BY CareUnitHSAID OFFSET 1 ROWS FETCH NEXT 2 ROWS ONLY ;
                preparedStatement.setLong(1, offset);
                preparedStatement.setLong(2, limit);
            }
            else{
                int p = 0;
                StringBuilder sb = new StringBuilder("select * from lernen.product where ");
                if(filterProduct.getFilterById() > 0){
                    sb.append("id = ? ");
                }
                else if(null != filterProduct.getFilterByName()){
                    sb.append("name like %?% ");
                    p = 1;
                }
                else if(0.0 != filterProduct.getFiltyByCost()){
                    sb.append("cost = ? ");
                    p = 2;
                }
                sb.append(" order by id limit ?,? ");
                System.out.println("Quer is = " + sb.toString());
                preparedStatement = connection.prepareStatement(sb.toString());
                switch (p){
                    case 0:
                        preparedStatement.setInt(1, filterProduct.getFilterById());
                        break;
                    case 1:
                        preparedStatement.setString(1, filterProduct.getFilterByName());
                        break;
                    case 2:
                        preparedStatement.setFloat(1, filterProduct.getFiltyByCost());
                }
                preparedStatement.setLong(2, offset);
                preparedStatement.setLong(3, limit);
            }

            resultSet = preparedStatement.executeQuery();
            if(null != resultSet){
                while(resultSet.next()){
                    productList.add(new Product(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getFloat("cost")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(null != preparedStatement){
                    preparedStatement.close();
                }
                if(null != resultSet){
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return productList;
    }

    public long getCountProducts(FilterProduct filterProduct){
        long count = 0;
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select count(*) from lernen.product");
            if(null != resultSet && resultSet.next())
                count = resultSet.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
    int fetchCaller = 0;
    public List<Product>  getProductFromUntil(int offset, int limit, Comparator<Product> sorter, ProductFilter filter) {
        List<Product> productList = new LinkedList<>();
        try {
            preparedStatement = connection.prepareStatement("select * from lernen.product order by id limit ?,? ");
            System.out.println("(["+(++fetchCaller)+"]--select * from lernen.product order by id limit offset,limit = "+offset+","+limit+"--)");
            preparedStatement.setLong(1, offset);
            preparedStatement.setLong(2, limit);
            resultSet = preparedStatement.executeQuery();
            if(null != resultSet) {
                while (resultSet.next()) {
                    productList.add(new Product(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getFloat("cost")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(null != preparedStatement){
                    preparedStatement.close();
                }
                if(null != resultSet){
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return productList.stream().filter(filter).collect(Collectors.toList());
    }

    public int getCountProducts(ProductFilter filter) {
        return (int) getCountProducts(new FilterProduct());
    }
}
