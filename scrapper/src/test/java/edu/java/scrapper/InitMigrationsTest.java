package edu.java.scrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InitMigrationsTest extends IntegrationEnvironment {
    private final static String sql =
        "SELECT column_name FROM information_schema.columns WHERE table_name = ? ORDER BY ordinal_position;";

    private final static List<String> LINK_COLUMNS = List.of("id", "url", "type", "updated_at", "last_checked_at");
    private final static List<String> CHAT_COLUMNS = List.of("id");
    private final static List<String> ASSOCIATION_COLUMNS = List.of("chat_id", "link_id");

    @Test
    @DisplayName("Проверка создания таблицы link")
    @SneakyThrows
    public void tableLinkCreateTest() {
        List<String> actualColumns = getTableColumns("link");
        assertThat(actualColumns).containsExactlyElementsOf(LINK_COLUMNS);
    }

    @Test
    @DisplayName("Проверка создания таблицы chat")
    @SneakyThrows
    public void tableChatCreateTest() {
        List<String> actualColumns = getTableColumns("chat");
        assertThat(actualColumns).containsExactlyElementsOf(CHAT_COLUMNS);
    }

    @Test
    @DisplayName("Проверка создания таблицы chat_link_")
    @SneakyThrows
    public void tableAssociationCreateTest() {
        List<String> actualColumns = getTableColumns("chat_link");
        assertThat(actualColumns).containsExactlyElementsOf(ASSOCIATION_COLUMNS);
    }

    @SneakyThrows
    private List<String> getTableColumns(String tableName) {
        List<String> columns = new ArrayList<>();
        @Cleanup Connection connection = POSTGRES.createConnection("");
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tableName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    columns.add(resultSet.getString("column_name"));
                }
            }
        }
        return columns;
    }
}
