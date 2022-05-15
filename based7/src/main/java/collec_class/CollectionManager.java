package collec_class;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.Stack;

@XmlRootElement(name = "storage2")
@XmlAccessorType(XmlAccessType.FIELD)
public class CollectionManager<E extends Route> implements Serializable {
    @XmlElement(name="route")
    private Stack<E> storage;
    private static final long serialVersionUID = 100L;
    private String collectionName= "routes";
    private final Date creationDate = new Date();
    public CollectionManager(Stack <E> storage){
        this.storage = storage;
    }
    public CollectionManager(){
        this.storage = new Stack<>();
    }
    public Date getCreationDate() {
        return creationDate;
    }
    public Stack<E> getStorage() {
        return storage;
    }
    public void setStorage(Stack<E> storage) {
        this.storage = storage;
    }
    public String filterwithname(String name){
        StringBuilder ans = new StringBuilder();
        for(E e:storage){
            if(e.get_Name().startsWith(name)){
                ans.append(e);
            }

        }
        return(ans.toString());
    }
    public E find_by_id(int id){
        for (E e: storage) {
            if (e.get_ID()==id){
                return(e);
            }
        }
        return(null);
    }
    public void remove_lower(E k, String userName){
        Stack<E> stack = new Stack<>();
        storage.stream().forEach(e -> {if (e.compareTo(k)<0&&e.getUserName().equals(userName)){stack.add(e);}});
        for (E e: stack
             ) {
            storage.remove(e);

        }
    }
    public void reorder(){
        Stack<E> temp2 = storage;
        Stack<E> temp = new Stack<>();
        for (int i = 0; i <= storage.size(); i++) {
            E e = storage.pop();
            temp.push(e);
        }
        storage = temp;

    }

    public Long sum_distance(){
        Long sum = 0L;
        for (E e: storage) {
            sum += e.getDistance();
        }
        return(sum);
    }

    public Stack<E> findbyAuthor(String userName){
        Stack<E> authorStack = new Stack<>();
        for(E e : storage){
            if(e.getUserName().equals(userName)){
                authorStack.add(e);
            }
        }
        return(authorStack);
    }
    public void add(E e){
        storage.add(e);
    }


    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }
}
