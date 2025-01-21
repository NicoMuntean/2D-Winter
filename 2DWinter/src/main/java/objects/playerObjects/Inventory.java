
package objects.playerObjects;


public class Inventory {
    
    int[] itemID;
    int[] itemQuantity;
    int selectedSlot;
    
    public Inventory() {
        
        itemID = new int[] {-1,-1,-1,-1,-1,-1,-1,-1,-1};
        itemQuantity = new int[] {0,0,0,0,0,0,0,0,0};
        selectedSlot = 0;        
        
    }
    
    public int[] getItemIDs() {
        return itemID;
    }
    
    public int getItemIDAtIndex(int index) {
        return itemID[index];
    }
    
    public int[] getItemQuantities() {
        return itemQuantity;
    }
    
    public int getItemQuantityAtIndex(int index) {
        return itemQuantity[index];
    }
    
    public boolean checkIfNewItemtypeAddable() {
        
        for(int i : itemID) {
            if(i == -1) {
                return true;
            }
        }
         
        return false;
    }
    
    public boolean checkIfItemAddable(int item) {
        
        for(int i = 0; i < itemID.length; i++) {
            if(itemID[i] == -1 || itemID[i] == item) {
                if(itemQuantity[i] < 256) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean checkIfItemAvaiable(int item) {
        for(int i : itemID) {
            if(i == item)
                return true;
        }
        
        return false;
    }
    
    public void addItem(int item, int quantity) {
        
        boolean itemAdded = false;
        
        for(int i = 0; i < itemID.length; i++) {
            
            if(itemID[i] == item && itemQuantity[i] < 256) {
                itemQuantity[i] += quantity;
                if(itemQuantity[i] > 256)
                    itemQuantity[i] = 256;
                itemAdded = true;
                break;
            } 
        }
        
        if(!itemAdded) {
            for(int i = 0; i < itemID.length; i++) {
                if(itemID[i] == -1) {
                    itemID[i] = item;
                    itemQuantity[i] = quantity;
                    if(itemQuantity[i] > 256)
                        itemQuantity[i] = 256;
                    break;
                }
            }
        } 
    }
    
    public void removeItem(int item) {
        
        if(itemID[selectedSlot] == item && itemQuantity[selectedSlot] > 0) {
            itemQuantity[selectedSlot]--;
                if(itemQuantity[selectedSlot] == 0) {
                    itemID[selectedSlot] = -1;
                }
        }
        
    } 
    
    public void pivotSelectedSlot(int dir) {
        
        if(dir == 0) {
            selectedSlot++;
            if(selectedSlot > 8) {
                selectedSlot = 0;
            }
        }
        else {
            selectedSlot--;
            if(selectedSlot < 0)
                selectedSlot = 8;
        }
    }
    
    public void setSelectedSlot(int nSelect) {
        selectedSlot = nSelect;
    }
    
    public int getSelectedSlot() {
        return selectedSlot;
    }
}
