package product.beans;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

@Named("user")
@SessionScoped
public class UserManager implements Serializable {
    private ProductsBean productsBean;
    FacesContext fCtx = FacesContext.getCurrentInstance();
    HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
    String sessionId = session.getId();
    private HtmlDataTable table;
    private int rowsOnPage;
    private String nameCriteria = "";
    private String priceCriteria = "all";
    private ArrayList<Products> purchasedProducts = new ArrayList<>();
    private ArrayList<Products> filteredProducts = new ArrayList<>();

    public ArrayList<Products> getFilteredProducts() {
        return filteredProducts;
    }

    public UserManager(){
        productsBean = ProductsBean.getSingleton();
        System.out.println("Id usuario");
        System.out.println(sessionId);
        rowsOnPage = 5;

        setPurchasedProducts(productsBean.getProductCopy());
        setFilteredProducts(new ArrayList<>(purchasedProducts));

        System.out.println("PURCHASED IS: " + getPurchasedProducts());
        System.out.println("FILTERED IS: "+ getFilteredProducts());
        /*
        try {
            readProductData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    private void getDataBetweenReq() {
    }
    /*
    private void getDataBetweenReq() {
        if (getFilteredProducts().size() > 0 && !(priceCriteria.equals("all") || nameCriteria.equals(""))) {
            purchasedProducts.clear();
            purchasedProducts.addAll(getFilteredProducts());
        }
    }
    */

    /*
    private void getDataBetweenReq() {
        if (productsBean.getFilteredProducts().size() > 0 && !(priceCriteria.equals("all") || nameCriteria.equals(""))) {
            purchasedProducts.clear();
            purchasedProducts.addAll(productsBean.getFilteredProducts());
        }
    }*/

    public ProductsBean getProductsBean() {
        return productsBean;
    }

    public void setProductsBean(ProductsBean productsBean) {
        this.productsBean = productsBean;
    }

    public ArrayList<Products> getProducts() {
        return purchasedProducts;
    }
    /*
    public ArrayList<Products> getProducts() {
        return productsBean.getProductData();
    }
    */

    public void setProducts(ArrayList<Products> products) {
        this.productsBean.setProductData(products);
    }

    public ArrayList<Products> getPurchasedProducts() {
        return purchasedProducts;
    }

    public void setPurchasedProducts(ArrayList<Products> purchasedProducts) {
        //this.purchasedProducts.addAll(purchasedProducts);
        this.purchasedProducts = new ArrayList<>(purchasedProducts);
    }

    public double getTotalPrice() {
        int qnty = 0;
        for(Products p : purchasedProducts){
            qnty += p.getTotalPrice();
        }
        return qnty;
    }

    /*
    public void readProductData() throws IOException {
        if (productsBean.getProductData().isEmpty()) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("Products.csv");

            InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader);
            for (String line; (line = reader.readLine()) != null; ) {
                String[] data = line.split(",");
                productsBean.getProductData().add(new Products(data[0], data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Double.parseDouble(data[5])));
            }
        }
        setPurchasedProducts(productsBean.getProductData());
    }
    */

    public void setProductUnits(Products p){
        Iterator<Products> iter = purchasedProducts.iterator();


        while (iter.hasNext()){
            Products products = iter.next();

            if(products.getSerialNum().equals(p.getSerialNum()) && p.getPurchaseNum() >= 0){
                System.out.println("PURCHASE VALID, NUM IS: " + p.getPurchaseNum());
                products.setPurchaseNum(p.getPurchaseNum());
                products.setTotalPrice();
            }
        }

//        User user = new User(sessionId, purchasedProducts);
//        if(!productsBean.getUsers().contains(user)){
//            productsBean.getUsers().add(user);
//            System.out.println("Users");
//            System.out.println(productsBean.getUsers());
//        }
    }

    public void viewProductData(){
//        System.out.println("user");
//        System.out.println(username);

        System.out.println("Products");
        for(Products p: purchasedProducts){
            System.out.println(getTotalPrice());
        }
    }

    /*
    public void viewProductData(){
//        System.out.println("user");
//        System.out.println(username);

        System.out.println("Products");
        for(Products p: productsBean.getProductData()){
            System.out.println(getTotalPrice());
        }
    }
    */

    public void validatePurchae(FacesContext arg0, UIComponent arg1, Object arg2)
            throws ValidatorException

    {
        if ((int)arg2 > -1) {
            //throw new ValidatorException(new FacesMessage("Al menos 5 caracteres "));
        }else {
            //FacesMessage error = new FacesMessage("Solo valores positivos");
            //FacesContext.getCurrentInstance().addMessage(null, error);
            throw new ValidatorException(new FacesMessage("Solo Valores Positivos "));
        }
    }

    public void localeChanged(ValueChangeEvent e) {

        System.out.println("HOLA "+e.getNewValue().toString());
        int temp= Integer.parseInt(e.getNewValue().toString());

        if (temp>-1) {
            //throw new ValidatorException(new FacesMessage("Al menos 5 caracteres "));
        }else {

            FacesMessage error = new FacesMessage("Error: '"+e.getNewValue().toString()+"' Solo valores positivos");
            error.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, error);

        }

    }


    public String getPriceCriteria() {
        return priceCriteria;
    }

    public void setPriceCriteria(String criteria) {
        this.priceCriteria = criteria;
    }

    public String getNameCriteria() {
        return nameCriteria;
    }

    public void setNameCriteria(String criteria) {
        this.nameCriteria = criteria;
    }


    public HtmlDataTable getTable() {
        return table;
    }

    public void setTable(HtmlDataTable table) {
        this.table = table;
    }

    public void goToFirstPage() {
        getDataBetweenReq();
        table.setFirst(0);
    }

    public void goToPreviousPage() {
        getDataBetweenReq();
        table.setFirst(table.getFirst() - table.getRows());
    }

    public void goToNextPage() {
        getDataBetweenReq();
        table.setFirst(table.getFirst() + table.getRows());

    }

    public void goToLastPage() {
        getDataBetweenReq();
        int totalRows = table.getRowCount();
        int displayRows = table.getRows();
        int full = totalRows / displayRows;
        int modulo = totalRows % displayRows;

        if (modulo > 0) {
            table.setFirst(full * displayRows);
        } else {
            table.setFirst((full - 1) * displayRows);
        }
    }

    public int getRowsOnPage() {
        return rowsOnPage;
    }

    public void setRowsOnPage(int rowsOnPage) {
        this.rowsOnPage = rowsOnPage;
    }

    public void addTableFilter(){
        filteredProducts.clear();

        if(priceCriteria.equals("all") && nameCriteria.equals("")){
            setFilteredProducts(getPurchasedProducts());
            Collections.sort(filteredProducts, new Comparator<Products>() {
                @Override
                public int compare(Products key_1, Products key_2) {
                    return (Integer.parseInt(key_1.getSerialNum()) - Integer.parseInt(key_2.getSerialNum()));
                }
            });
            return;
        }

        Collections.sort(filteredProducts, new Comparator<Products>() {
            @Override
            public int compare(Products key_1, Products key_2) {
                return (int) (key_1.getPricePerUnit() - key_2.getPricePerUnit());
            }
        });

        if (priceCriteria.equals(">=10")) {
            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getPricePerUnit() < 10) {
                    filteredProducts.add(purchasedProducts.get(i));
                }
            }
        }

        if (priceCriteria.equals("<10")) {
            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getPricePerUnit() >= 10) {
                    filteredProducts.add(purchasedProducts.get(i));
                }
            }
        }

        if(!nameCriteria.equals("")){
            String[] temp;
            String filter = "";
            if(nameCriteria.contains("%")){
                temp = nameCriteria.split("%");
                filter = temp[0];
            } else {
                filter = nameCriteria;
            }

            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getProductName().contains(filter)) {
                    filteredProducts.add(purchasedProducts.get(i));
                }
            }
        }

        else {
            System.out.println("ENTERED HERE MY DAWG");
            setFilteredProducts(getPurchasedProducts());
        }
        table.setFirst(0);
    }

    public void setFilteredProducts(ArrayList<Products> newFiltered) {
        filteredProducts.clear();
        filteredProducts.addAll(newFiltered);
    }

    /*
    public void addTableFilter(){
        productsBean.clearAll();
        purchasedProducts.addAll(filteredProducts);
        filteredProducts.clear();

        if(priceCriteria.equals("all") && nameCriteria.equals("")){
            Collections.sort(purchasedProducts, new Comparator<Products>() {
                @Override
                public int compare(Products key_1, Products key_2) {
                    return (Integer.parseInt(key_1.getSerialNum()) - Integer.parseInt(key_2.getSerialNum()));
                }
            });
            return;
        }

        Collections.sort(purchasedProducts, new Comparator<Products>() {
            @Override
            public int compare(Products key_1, Products key_2) {
                return (int) (key_1.getPricePerUnit() - key_2.getPricePerUnit());
            }
        });

        if (priceCriteria.equals(">=10")) {
            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getPricePerUnit() < 10) {
                    filteredProducts.add(purchasedProducts.remove(i));
                    i = 0;
                }
            }
            productsBean.setFilteredProducts(purchasedProducts);
        }

        if (priceCriteria.equals("<10")) {
            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (products.getPricePerUnit() >= 10) {
                    filteredProducts.add(purchasedProducts.remove(i));
                    i = 0;
                }
            }
            productsBean.setFilteredProducts(purchasedProducts);
        }

        if(!nameCriteria.equals("")){
            String[] temp;
            String filter = "";
            if(nameCriteria.contains("%")){
                temp = nameCriteria.split("%");
                filter = temp[0];
            } else {
                filter = nameCriteria;
            }

            for (int i = 0; i < purchasedProducts.size(); i++) {
                Products products = purchasedProducts.get(i);
                if (!products.getProductName().contains(filter)) {
                    filteredProducts.add(purchasedProducts.remove(i));
                    i = 0;
                }
            }
        }

        table.setFirst(0);

    }
    */
}
