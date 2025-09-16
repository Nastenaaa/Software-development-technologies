package app.model;

import java.math.BigDecimal;

public class BudgetItem {
    private int id;
    private Budget budget;
    private Category category;
    private BigDecimal limit;

    public BudgetItem() {}

    public BudgetItem(int id, Budget budget, Category category, BigDecimal limit) {
        this.id = id;
        this.budget = budget;
        this.category = category;
        this.limit = limit;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Budget getBudget() { return budget; }
    public void setBudget(Budget budget) { this.budget = budget; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public BigDecimal getLimit() { return limit; }
    public void setLimit(BigDecimal limit) { this.limit = limit; }

    @Override
    public String toString() {
        return "BudgetItem{id=" + id +
                ", budget=" + (budget != null ? budget.getId() : null) +
                ", category=" + (category != null ? category.getId() : null) +
                ", limit=" + limit + "}";
    }
}
