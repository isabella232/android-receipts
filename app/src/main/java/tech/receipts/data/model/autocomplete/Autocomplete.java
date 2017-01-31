package tech.receipts.data.model.autocomplete;

public class Autocomplete {
    private String tax;
    private String pos;

    public Autocomplete(String tax, String pos) {
        this.tax = tax;
        this.pos = pos;
    }

    public String getTax() {
        return tax;
    }

    public String getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return "Autocomplete{" +
                "tax='" + tax + '\'' +
                ", pos='" + pos + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Autocomplete that = (Autocomplete) o;

        return tax != null ? tax.equals(that.tax) : that.tax == null && (pos != null ? pos.equals(that.pos) : that.pos == null);
    }

    @Override
    public int hashCode() {
        int result = tax != null ? tax.hashCode() : 0;
        result = 31 * result + (pos != null ? pos.hashCode() : 0);
        return result;
    }
}
