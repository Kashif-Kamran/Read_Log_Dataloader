package io.project.betterreadsdataloader.book;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.CassandraType.Name;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;
import java.util.List;
@Table(value = "book_by_id")
public class Book
{
    @Id
    @PrimaryKeyColumn(name = "book_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    @Column("book_name")
    @CassandraType(type = Name.TEXT)
    private String name;

    @Column("book_description")
    @CassandraType(type = Name.TEXT)
    private String description;

    @Column("publish_date")
    @CassandraType(type = Name.DATE)
    private LocalDate published_date;

    @Column("covers_ids")
    @CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
    private List<String> coversIds;

    @Column("author_ids")
    @CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
    private List<String> authorsIds;

    @Column("author_names")
    @CassandraType(type = Name.LIST, typeArguments = Name.TEXT)
    private List<String> authorsNames;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LocalDate getPublished_date()
    {
        return published_date;
    }

    public void setPublished_date(LocalDate published_date)
    {
        this.published_date = published_date;
    }

    public List<String> getCoversIds()
    {
        return coversIds;
    }

    public void setCoversIds(List<String> coversIds)
    {
        this.coversIds = coversIds;
    }

    public List<String> getAuthorsIds()
    {
        return authorsIds;
    }

    public void setAuthorsIds(List<String> authorsIds)
    {
        this.authorsIds = authorsIds;
    }

    public List<String> getAuthorsNames()
    {
        return authorsNames;
    }

    public void setAuthorsNames(List<String> authorsNames)
    {
        this.authorsNames = authorsNames;
    }

    @Override
    public String toString()
    {
        return "Book{" +
                "id='" + id + '\n' +
                ", name='" + name + '\n' +
                ", description='" + description + '\n' +
                ", published_date=" + published_date + '\n' +
                ", coversIds=" + coversIds + '\n' +
                ", authorsIds=" + authorsIds + '\n' +
                ", authorsNames=" + authorsNames + '\n' +
                '}';
    }
}
