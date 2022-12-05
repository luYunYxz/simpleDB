package simpledb.storage;

import simpledb.common.Type;

import java.io.Serializable;
import java.util.*;

/**
 * TupleDesc describes the schema of a tuple.
 * 数据字典.表示每张表中有哪些字段.
 */
public class TupleDesc implements Serializable {

    /**
     * A help class to facilitate organizing the information of each field
     * 指定了当前的
     */
    public static class TDItem implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * The type of the field
         * 字段类型
         */
        public final Type fieldType;

        /**
         * The name of the field
         * 字段名称
         */
        public final String fieldName;

        public TDItem(Type t, String n) {
            this.fieldName = n;
            this.fieldType = t;
        }

        public String toString() {
            return fieldName + "(" + fieldType + ")";
        }
    }

    private List<TDItem> tdItems = new ArrayList<>();

    /**
     * @return An iterator which iterates over all the field TDItems
     * that are included in this TupleDesc
     * 便利当前数据字典中的每一条记录.
     */
    public Iterator<TDItem> iterator() {
        return tdItems.iterator();
    }

    private static final long serialVersionUID = 1L;

    /**
     * Create a new TupleDesc with typeAr.length fields with fields of the
     * specified types, with associated named fields.
     *
     * @param typeAr  array specifying the number of and types of fields in this
     *                TupleDesc. It must contain at least one entry.
     * @param fieldAr array specifying the names of the fields. Note that names may
     *                be null.
     */
    public TupleDesc(Type[] typeAr, String[] fieldAr) {
        for (int i = 0; i < typeAr.length; i++) {
            tdItems.add(new TDItem(typeAr[i], fieldAr[i]));
        }
    }

    /**
     * Constructor. Create a new tuple desc with typeAr.length fields with
     * fields of the specified types, with anonymous (unnamed) fields.
     * <p>
     * 匿名内部的 数据字典.其中只有类型,没有字段名称
     *
     * @param typeAr array specifying the number of and types of fields in this
     *               TupleDesc. It must contain at least one entry.
     */
    public TupleDesc(Type[] typeAr) {
        for (int i = 0; i < typeAr.length; i++) {
            this.tdItems.add(new TDItem(typeAr[i], null));
        }
    }

    /**
     * 返回当前数据字典中有的记录行数
     *
     * @return the number of fields in this TupleDesc
     */
    public int numFields() {
        return this.tdItems.size();
    }

    /**
     * Gets the (possibly null) field name of the ith field of this TupleDesc.
     * 获取到第 i行的记录条数.从0. 这个第i行元素可能为null .
     * 这个是一个奇怪的实现.
     *
     * @param i index of the field name to return. It must be a valid index.
     * @return the name of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public String getFieldName(int i) throws NoSuchElementException {

        if (i < 0 || i >= this.tdItems.size()) {
            throw new NoSuchElementException("第i行元素不存在");
        }
        return this.tdItems.get(i).fieldName;
    }

    /**
     * Gets the type of the ith field of this TupleDesc.
     * 获取到第i裂的类型
     *
     * @param i The index of the field to get the type of. It must be a valid
     *          index.
     * @return the type of the ith field
     * @throws NoSuchElementException if i is not a valid field reference.
     */
    public Type getFieldType(int i) throws NoSuchElementException {
        if (i < 0 || i >= this.tdItems.size()) {
            throw new NoSuchElementException("第i个元素不存在");
        }
        return this.tdItems.get(i).fieldType;
    }

    /**
     * Find the index of the field with a given name.
     * 根据name获取到 记录的rank.
     *
     * @param name name of the field.
     * @return the index of the field that is first to have the given name.
     * @throws NoSuchElementException if no field with a matching name is found.
     */
    public int fieldNameToIndex(String name) throws NoSuchElementException {
        if (name == null) {
            throw new IllegalArgumentException("fieldName 不能 为空");
        }
        for (int i = 0; i < this.tdItems.size(); i++) {
            if (name.equals(this.tdItems.get(i).fieldName)) {
                return i;
            }
        }
        throw new NoSuchElementException("名称为name的字段不存在");
    }

    /**
     * @return The size (in bytes) of tuples corresponding to this TupleDesc.
     * Note that tuples from a given TupleDesc are of a fixed size.
     */
    public int getSize() {
        return this.tdItems.size();
    }

    /**
     * Merge two TupleDescs into one, with td1.numFields + td2.numFields fields,
     * with the first td1.numFields coming from td1 and the remaining from td2.
     * 将两个 TupleDesc合并为一个,其中合并到第1个.
     *
     * @param td1 The TupleDesc with the first fields of the new TupleDesc
     * @param td2 The TupleDesc with the last fields of the TupleDesc
     * @return the new TupleDesc
     */
    public static TupleDesc merge(TupleDesc td1, TupleDesc td2) {
        if (td2 == null || td2.getSize() == 0) {
            return td1;
        }
        td1.tdItems.addAll(td2.tdItems);
        return td1;
    }

    /**
     * Compares the specified object with this TupleDesc for equality. Two
     * TupleDescs are considered equal if they have the same number of items
     * and if the i-th type in this TupleDesc is equal to the i-th type in o
     * for every i.
     * <p>
     * 比较两个 TypeDesc是否完全相等,第一条是记录相等,第二条是每一条类型和名称都相等.
     *
     * @param o the Object to be compared for equality with this TupleDesc.
     * @return true if the object is equal to this TupleDesc.
     */


    @Override
    public boolean equals(Object o) {

        if (!(o instanceof TupleDesc)) {
            return false;
        }
        TupleDesc that = (TupleDesc) o;
        if (this.getSize() != that.getSize()) {
            return false;
        }
        for (int i = 0; i < this.tdItems.size(); i++) {
            TDItem thisItem = this.tdItems.get(i);
            TDItem thatItem = that.tdItems.get(i);
            //todo 比较两个 tdItem是否相等
        }
        return false;
    }

    /**
     * 比较两个对象的hahsCode是否相等.
     *
     * @return
     */
    public int hashCode() {
        // If you want to use TupleDesc as keys for HashMap, implement this so
        // that equal objects have equals hashCode() results
        throw new UnsupportedOperationException("unimplemented");
    }

    /**
     * Returns a String describing this descriptor. It should be of the form
     * "fieldType[0](fieldName[0]), ..., fieldType[M](fieldName[M])", although
     * the exact format does not matter.
     *
     * @return String describing this descriptor.
     */
    public String toString() {
        // some code goes here
        return "";
    }
}
