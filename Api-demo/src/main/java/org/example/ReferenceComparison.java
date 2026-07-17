package org.example;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceComparison {

    public void putReference(Reference ref){


    }
    public static void main(String[] args) {
        // Strong reference
        String strongObject = new String("ABC");

        // Weak Reference
        WeakReference<Object> weakRef = new WeakReference<>(strongObject);
        strongObject = null; // Remove strong reference
        System.gc(); // Request garbage collection
        System.out.println("Weak reference after GC: " + weakRef.get()); // Likely null

        // Soft Reference
        Object softReferent = new String("ABC");
        SoftReference<Object> softRef = new SoftReference<>(softReferent);
        softReferent = null; // Remove strong reference
        System.gc(); // Request garbage collection
        System.out.println("Soft reference after GC (no memory pressure): " + softRef.get()); // May still hold the object

        // To demonstrate SoftReference clearing, you'd need to simulate memory pressure,
        // which is harder to guarantee in a simple example.
    }
}