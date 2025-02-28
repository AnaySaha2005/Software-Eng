package Q1;
import java.io.FileWriter;
import java.util.*;

class ItemInfo {
    String name;
    int stock;
    double amount;

    public ItemInfo(String name, int stock, double amount) {
        this.name = name;
        this.stock = stock;
        this.amount = amount;
    }

}

public class Inventory {
    private HashMap<String, ItemInfo> ItemList;

    public Inventory() {
        ItemList = new HashMap<>();
    }

    public boolean contains(String item) {
        return ItemList.containsKey(item);
    }

    public void addItem(String item, int stock, double amount) {
        try (FileWriter fw = new FileWriter("./log.txt", true)) {
            if (ItemList.containsKey(item)) {
                fw.write("Item already exists!!!");
            } else {
                ItemInfo info = new ItemInfo(item, stock, amount);
                ItemList.put(item, info);
                fw.write("Successfully added " + item);
            }
            fw.write("\n");
            fw.flush();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void updateStock(String item, int stock) {
        ItemInfo info = ItemList.get(item);
        info.stock += stock;
        ItemList.put(item, info);
        try (FileWriter fw = new FileWriter("./log.txt", true)) {
            fw.write("Successfully updated " + item);
            fw.write("\n");
            fw.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int removeItem(String item, int requirement) {
        try (FileWriter fw = new FileWriter("./log.txt", true)) {
            if (!ItemList.containsKey(item)) {
                fw.write("Item is not present in stock");
                fw.write("\n");
                fw.flush();
                return 0;
            }
            ItemInfo info = ItemList.get(item);
            if (info.stock < requirement) {
                fw.write("Only " + info.stock + " of " + item + " is available.");
            } else {
                info.stock -= requirement;
                if (info.stock == 0)
                    ItemList.remove(item);
                fw.write("Successfully removed " + requirement + " " + item + ".");
                fw.write("\n");
                fw.flush();
                return requirement;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public void viewInventory() {
        for (String item : ItemList.keySet()) {
            ItemInfo info = ItemList.get(item);
            System.out.println(
                    "Item:\t" + info.name + "\nInstock:\t" + info.stock + "\nAmount(per piece):\t" + info.amount);
            System.out.println("----------------------------------------------");
        }
    }
}

class Seller {
    Inventory inventory;
    String name;

    public Seller(String name) {
        this.name = name;
        inventory = new Inventory();
    }

    void buyItem(String item, int stockIn, double amount) {
        inventory.addItem(item, stockIn, amount);
    }

    int sellItem(String item, int requirement) {
        return inventory.removeItem(item, requirement);

    }

    void updateStock(String item, int stockIn) {
        if (inventory.contains(item))
            inventory.updateStock(item, stockIn);
        else
            System.out.println("Item doesn't exist in stock!!");
    }

    void viewInventory() {
        inventory.viewInventory();
    }
}

class Main {
    public static void main(String[] args) {
        Seller seller = new Seller("Anay");
        seller.buyItem("Mangoes", 10, 10);
        seller.buyItem("Apples", 12, 10);
        seller.buyItem("Guavas", 14, 10);
        seller.buyItem("Grapes", 11, 10);
        seller.buyItem("Oranges", 7, 10);
        seller.buyItem("Chocolates", 20, 10);
        seller.viewInventory();
        seller.updateStock("Oranges", 30);
        // customer
        seller.sellItem("Mangoes", 10);
        seller.sellItem("Peaches", 5);
        seller.sellItem("Guavas", 22);

    }

}