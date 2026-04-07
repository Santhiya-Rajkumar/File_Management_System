
import java.util.ArrayList;
import java.util.Scanner;

// Abstract base class
abstract class FileItem {

    protected String name;
    protected String createdDate;

    public FileItem(String name) {
        this.name = name;
        this.createdDate = java.time.LocalDate.now().toString();
    }

    public String getName() {
        return name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public abstract void display();

    public abstract void delete();
}

// File class
class File extends FileItem {

    private String content;

    public File(String name, String content) {
        super(name);
        this.content = content;
    }

    @Override
    public void display() {
        System.out.println("File: " + name + " | Created: " + createdDate);
        System.out.println("Content: " + content);
    }

    @Override
    public void delete() {
        System.out.println("File " + name + " deleted.");
    }

    public void editContent(String newContent) {
        this.content = newContent;
        System.out.println("File " + name + " updated.");
    }
}

// Folder class
class Folder extends FileItem {

    private ArrayList<FileItem> items;

    public Folder(String name) {
        super(name);
        items = new ArrayList<>();
    }

    public void addItem(FileItem item) {
        items.add(item);
    }

    public void listItems() {
        if (items.isEmpty()) {
            System.out.println("(empty)");
        } else {
            for (FileItem item : items) {
                System.out.println("- " + item.getName());
            }
        }
    }

    public FileItem search(String name) {
        for (FileItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
            if (item instanceof Folder) {
                FileItem found = ((Folder) item).search(name);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    public void deleteItem(String name) {
        items.removeIf(item -> item.getName().equalsIgnoreCase(name));
        System.out.println("Deleted item: " + name);
    }

    public void moveItem(String name, Folder target) {
        FileItem toMove = null;
        for (FileItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                toMove = item;
                break;
            }
        }
        if (toMove != null) {
            items.remove(toMove);
            target.addItem(toMove);
            System.out.println("Moved " + name + " to " + target.getName());
        } else {
            System.out.println("Item not found.");
        }
    }

    @Override
    public void display() {
        System.out.println("Folder: " + name + " | Created: " + createdDate);
        listItems();
    }

    @Override
    public void delete() {
        System.out.println("Folder " + name + " deleted with all contents.");
        items.clear();
    }
}

// Main App
public class FileManagementSystem {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Folder root = new Folder("Root");

        while (true) {
            System.out.println("\n--- File Management System ---");
            System.out.println("1. Create File");
            System.out.println("2. Create Folder");
            System.out.println("3. Display Root");
            System.out.println("4. Search Item");
            System.out.println("5. Delete Item");
            System.out.println("6. Edit File");
            System.out.println("7. Move Item");
            System.out.println("8. Exit");
            System.out.print("Choose: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter file name: ");
                    String fname = sc.nextLine();
                    System.out.print("Enter content: ");
                    String content = sc.nextLine();
                    root.addItem(new File(fname, content));
                    break;
                case 2:
                    System.out.print("Enter folder name: ");
                    String folderName = sc.nextLine();
                    root.addItem(new Folder(folderName));
                    break;
                case 3:
                    root.display();
                    break;
                case 4:
                    System.out.print("Enter item name to search: ");
                    String searchName = sc.nextLine();
                    FileItem found = root.search(searchName);
                    if (found != null) {
                        found.display();
                    } else {
                        System.out.println("Item not found.");
                    }
                    break;
                case 5:
                    System.out.print("Enter item name to delete: ");
                    String delName = sc.nextLine();
                    root.deleteItem(delName);
                    break;
                case 6:
                    System.out.print("Enter file name to edit: ");
                    String editName = sc.nextLine();
                    FileItem item = root.search(editName);
                    if (item instanceof File) {
                        System.out.print("Enter new content: ");
                        String newContent = sc.nextLine();
                        ((File) item).editContent(newContent);
                    } else {
                        System.out.println("Not a file.");
                    }
                    break;
                case 7:
                    System.out.print("Enter item name to move: ");
                    String moveName = sc.nextLine();
                    System.out.print("Enter target folder name: ");
                    String targetName = sc.nextLine();
                    FileItem target = root.search(targetName);
                    if (target instanceof Folder) {
                        root.moveItem(moveName, (Folder) target);
                    } else {
                        System.out.println("Target folder not found.");
                    }
                    break;
                case 8:
                    System.out.println("Exiting...");
                    sc.close();
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
