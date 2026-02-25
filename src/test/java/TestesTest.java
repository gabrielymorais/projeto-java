import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestesTest {

    private static final double EPS = 1e-9;

    // TESTES NA CASSE DATE
    // Testa se o construtor aceita uma data válida e se getters/toString retornam exatamente o esperado.
    @Test
    @DisplayName("Date: cria data válida e getters/toString funcionam")
    void date_valida_getters_toString() {
        Date d = new Date(11, 30, 2024);
        assertEquals(11, d.getMonth());
        assertEquals(30, d.getDay());
        assertEquals(2024, d.getYear());
        assertEquals("11/30/2024", d.toString());
    }

    // Testa se o construtor impede meses fora do intervalo 1..12 (garante validação de limite).
    @Test
    @DisplayName("Date: mês inválido lança IllegalArgumentException")
    void date_mes_invalido() {
        assertThrows(IllegalArgumentException.class, () -> new Date(0, 10, 2024));
        assertThrows(IllegalArgumentException.class, () -> new Date(13, 10, 2024));
    }

    // Testa se o construtor rejeita dia inválido (ex.: dia 0) para um mês comum.
    @Test
    @DisplayName("Date: dia inválido para mês comum lança IllegalArgumentException")
    void date_dia_invalido_mes_comum() {
        assertThrows(IllegalArgumentException.class, () -> new Date(5, 0, 2024));
    }

    // Testa a regra de ano bissexto: 29/02 deve ser permitido quando o ano é bissexto.
    @Test
    @DisplayName("Date: 29/02 em ano bissexto é permitido")
    void date_leap_year_ok() {
        Date d = new Date(2, 29, 2024);
        assertEquals("2/29/2024", d.toString());
    }

    // Testa a regra de ano não-bissexto: 29/02 deve lançar exceção quando o ano não é bissexto.
    @Test
    @DisplayName("Date: 29/02 em ano não-bissexto lança IllegalArgumentException")
    void date_leap_year_invalido() {
        assertThrows(IllegalArgumentException.class, () -> new Date(2, 29, 2023));
    }

    // Teste de caracterização: documenta um bug atual (mês 12 provoca ArrayIndexOutOfBoundsException).
    @Test
    @DisplayName("Date: BUG atual - mês 12 causa ArrayIndexOutOfBoundsException (documentado)")
    void date_bug_mes_12_documentado() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> new Date(12, 1, 2024));
    }


    // TESTES NAS CLASSES EMPLOYEE (abstrata) - DummyEmployee para testar o "super"

    // Classe auxiliar: permite instanciar Employee (que é abstrata) para testar getters e toString da classe base.
    private static class DummyEmployee extends Employee {
        public DummyEmployee(String fn, String ln, String ssn, Date bd) {
            super(fn, ln, ssn, bd);
        }

        @Override
        public double getPaymentAmount() {
            return 0.0;
        }
    }

    // Testa se a classe base Employee retorna corretamente os dados básicos (nome/ssn) e monta um toString coerente.
    @Test
    @DisplayName("Employee: getters e toString do super funcionam")
    void employee_getters_toString() {
        Date bd = new Date(6, 28, 1989);
        Employee e = new DummyEmployee("Ana", "Silva", "111-11-1111", bd);

        assertEquals("Ana", e.getFirstName());
        assertEquals("Silva", e.getLastName());
        assertEquals("111-11-1111", e.getSocialSecurityNumber());

        String s = e.toString();
        assertTrue(s.contains("Ana Silva"));
        assertTrue(s.contains("social security number: 111-11-1111"));
    }

    // =========================================================
    // Invoice
    // =========================================================

    // Testa o cálculo principal de Invoice: pagamento deve ser quantity * pricePerItem, e getters devem refletir o construtor.
    @Test
    @DisplayName("Invoice: payment = quantity * pricePerItem")
    void invoice_payment_ok() {
        Invoice inv = new Invoice("01234", "seat", 2, 375.00);
        assertEquals("01234", inv.getPartNumber());
        assertEquals("seat", inv.getPartDescription());
        assertEquals(2, inv.getQuantity());
        assertEquals(375.00, inv.getPricePerItem(), EPS);
        assertEquals(750.00, inv.getPaymentAmount(), EPS);
    }

    // Testa se os setters mudam o estado corretamente e se validam valores inválidos (negativos).
    @Test
    @DisplayName("Invoice: setters atualizam e validam quantidade e preço")
    void invoice_setters_validacoes() {
        Invoice inv = new Invoice("X", "Y", 1, 10.0);

        inv.setQuantity(5);
        assertEquals(5, inv.getQuantity());
        assertEquals(50.0, inv.getPaymentAmount(), EPS);

        inv.setPricePerItem(2.5);
        assertEquals(2.5, inv.getPricePerItem(), EPS);
        assertEquals(12.5, inv.getPaymentAmount(), EPS);

        assertThrows(IllegalArgumentException.class, () -> inv.setQuantity(-1));
        assertThrows(IllegalArgumentException.class, () -> inv.setPricePerItem(-0.01));
    }

    // Testa se o construtor impede criar Invoice com parâmetros inválidos (quantidade/preço negativos).
    @Test
    @DisplayName("Invoice: construtor valida parâmetros inválidos")
    void invoice_constructor_validacoes() {
        assertThrows(IllegalArgumentException.class, () -> new Invoice("A", "B", -1, 10.0));
        assertThrows(IllegalArgumentException.class, () -> new Invoice("A", "B", 1, -10.0));
    }

    // Testa se o toString do Invoice contém informações essenciais para leitura/relatório (nome, part number, descrição).
    @Test
    @DisplayName("Invoice: toString contém campos principais")
    void invoice_toString_contem_campos() {
        Invoice inv = new Invoice("01234", "seat", 2, 375.00);
        String s = inv.toString();
        assertTrue(s.toLowerCase().contains("invoice"));
        assertTrue(s.contains("01234"));
        assertTrue(s.contains("seat"));
    }

    // =========================================================
    // SalariedEmployee
    // =========================================================

    // Testa se o pagamento do assalariado é igual ao salário semanal, e se construtor/setter rejeitam valores negativos.
    @Test
    @DisplayName("SalariedEmployee: payment = weeklySalary e validações")
    void salaried_employee_ok() {
        SalariedEmployee e = new SalariedEmployee("John", "Doe", "999-99-9999", new Date(1, 10, 2020), 1000.0);
        assertEquals(1000.0, e.getWeeklySalary(), EPS);
        assertEquals(1000.0, e.getPaymentAmount(), EPS);

        e.setWeeklySalary(1200.0);
        assertEquals(1200.0, e.getWeeklySalary(), EPS);
        assertEquals(1200.0, e.getPaymentAmount(), EPS);

        assertThrows(IllegalArgumentException.class,
                () -> new SalariedEmployee("A", "B", "1", new Date(1, 10, 2020), -1.0));
        assertThrows(IllegalArgumentException.class, () -> e.setWeeklySalary(-0.01));

        String s = e.toString();
        assertTrue(s.toLowerCase().contains("salaried employee"));
        assertTrue(s.contains("weekly salary"));
    }

    // =========================================================
    // HourlyEmployee
    // =========================================================

    // Testa o cálculo sem hora extra: para hours < 40, pagamento deve ser wage * hours.
    @Test
    @DisplayName("HourlyEmployee: sem hora extra (hours < 40) => wage*hours")
    void hourly_employee_sem_extra() {
        HourlyEmployee e = new HourlyEmployee("Karen", "Price", "222-22-2222", new Date(6, 28, 1989), 20.0, 35.0);
        assertEquals(20.0, e.getWage(), EPS);
        assertEquals(35.0, e.getHours(), EPS);
        assertEquals(700.0, e.getPaymentAmount(), EPS);
    }

    // Testa o cálculo com hora extra: a partir de 40h, paga 40*wage + (hours-40)*wage*1.5.
    @Test
    @DisplayName("HourlyEmployee: com hora extra (hours >= 40) => 40*wage + (hours-40)*wage*1.5")
    void hourly_employee_com_extra() {
        HourlyEmployee e = new HourlyEmployee("Karen", "Price", "222-22-2222", new Date(6, 28, 1989), 20.0, 45.0);
        assertEquals(40 * 20.0 + 5 * 20.0 * 1.5, e.getPaymentAmount(), EPS);
    }

    // Testa validações de domínio: construtor impede wage/horas inválidos e setters impedem valores fora das regras.
    @Test
    @DisplayName("HourlyEmployee: validações em construtor e setters")
    void hourly_employee_validacoes() {
        assertThrows(IllegalArgumentException.class,
                () -> new HourlyEmployee("A", "B", "1", new Date(1, 10, 2020), -0.01, 10));

        assertThrows(IllegalArgumentException.class,
                () -> new HourlyEmployee("A", "B", "1", new Date(1, 10, 2020), 10, -1));

        assertThrows(IllegalArgumentException.class,
                () -> new HourlyEmployee("A", "B", "1", new Date(1, 10, 2020), 10, 169));

        HourlyEmployee e = new HourlyEmployee("A", "B", "1", new Date(1, 10, 2020), 10, 10);

        assertThrows(IllegalArgumentException.class, () -> e.setWage(0.0));
        assertThrows(IllegalArgumentException.class, () -> e.setWage(-1.0));
        e.setWage(15.0);
        assertEquals(15.0, e.getWage(), EPS);

        assertThrows(IllegalArgumentException.class, () -> e.setHours(0.0));
        assertThrows(IllegalArgumentException.class, () -> e.setHours(168.0));
        e.setHours(20.0);
        assertEquals(20.0, e.getHours(), EPS);
    }

    // =========================================================
    // CommissionEmployee
    // =========================================================

    // Testa o cálculo principal do comissionado: payment deve ser commissionRate * grossSales.
    @Test
    @DisplayName("CommissionEmployee: payment = commissionRate * grossSales")
    void commission_employee_payment_ok() {
        CommissionEmployee e = new CommissionEmployee("Sue", "Jones", "333-33-3333", new Date(1, 26, 2018), 10000.0, 0.06);
        assertEquals(10000.0, e.getGrossSales(), EPS);
        assertEquals(0.06, e.getCommissionRate(), EPS);
        assertEquals(600.0, e.getPaymentAmount(), EPS);
    }

    // Testa regras de validação: construtor rejeita grossSales negativo e taxas inválidas; setters rejeitam/aceitam conforme regra.
    @Test
    @DisplayName("CommissionEmployee: validações de construtor e setters")
    void commission_employee_validacoes() {
        assertThrows(IllegalArgumentException.class,
                () -> new CommissionEmployee("A", "B", "1", new Date(1, 10, 2020), -1.0, 0.5));

        assertThrows(IllegalArgumentException.class,
                () -> new CommissionEmployee("A", "B", "1", new Date(1, 10, 2020), 10.0, 0.0));

        assertThrows(IllegalArgumentException.class,
                () -> new CommissionEmployee("A", "B", "1", new Date(1, 10, 2020), 10.0, 1.0));

        CommissionEmployee e = new CommissionEmployee("A", "B", "1", new Date(1, 10, 2020), 10.0, 0.5);

        assertThrows(IllegalArgumentException.class, () -> e.setGrossSales(-0.01));
        e.setGrossSales(200.0);
        assertEquals(200.0, e.getGrossSales(), EPS);

        e.setCommissionRate(0.0);
        assertEquals(0.0, e.getCommissionRate(), EPS);
        assertEquals(0.0, e.getPaymentAmount(), EPS);

        e.setCommissionRate(1.0);
        assertEquals(1.0, e.getCommissionRate(), EPS);
        assertEquals(200.0, e.getPaymentAmount(), EPS);

        assertThrows(IllegalArgumentException.class, () -> e.setCommissionRate(-0.01));
        assertThrows(IllegalArgumentException.class, () -> e.setCommissionRate(1.01));
    }

    // Testa se a saída textual do comissionado contém campos essenciais (identificação e valores-chave).
    @Test
    @DisplayName("CommissionEmployee: toString contém campos principais")
    void commission_employee_toString() {
        CommissionEmployee e = new CommissionEmployee("Sue", "Jones", "333-33-3333", new Date(1, 26, 2018), 10000.0, 0.06);
        String s = e.toString();
        assertTrue(s.toLowerCase().contains("commission employee"));
        assertTrue(s.contains("gross sales"));
        assertTrue(s.contains("commission rate"));
        assertTrue(s.contains("Sue Jones"));
    }

    // =========================================================
    // BasePlusCommissionEmployee
    // =========================================================

    // Testa o cálculo do "base + comissão": pagamento deve ser baseSalary + (commissionRate * grossSales).
    @Test
    @DisplayName("BasePlusCommissionEmployee: payment = baseSalary + (commissionRate*grossSales)")
    void base_plus_commission_payment_ok() {
        BasePlusCommissionEmployee e =
                new BasePlusCommissionEmployee("Bob", "Lewis", "444-44-4444", new Date(1, 25, 2023),
                        0.04, 5000.0, 300.0);

        assertEquals(300.0, e.getBaseSalary(), EPS);
        assertEquals(300.0 + (0.04 * 5000.0), e.getPaymentAmount(), EPS);
    }

    // Testa validações do salário base: construtor/setter devem rejeitar valores negativos e o toString deve indicar "base salary".
    @Test
    @DisplayName("BasePlusCommissionEmployee: validações de salário base (construtor e setter)")
    void base_plus_commission_validacoes() {
        assertThrows(IllegalArgumentException.class, () ->
                new BasePlusCommissionEmployee("A", "B", "1", new Date(1, 10, 2020),
                        0.1, 100.0, -1.0));

        BasePlusCommissionEmployee e =
                new BasePlusCommissionEmployee("A", "B", "1", new Date(1, 10, 2020),
                        0.1, 100.0, 10.0);

        assertThrows(IllegalArgumentException.class, () -> e.setBaseSalary(-0.01));
        e.setBaseSalary(50.0);
        assertEquals(50.0, e.getBaseSalary(), EPS);

        String s = e.toString();
        assertTrue(s.toLowerCase().contains("base-salaried"));
        assertTrue(s.toLowerCase().contains("base salary"));
    }

    // =========================================================
    // PieceWorker
    // =========================================================

    // Testa o cálculo por produção: pagamento = wage * pieces; também valida regras de não permitir valores negativos e confere toString.
    @Test
    @DisplayName("PieceWorker: payment = wage * pieces e validações")
    void piece_worker_ok() {
        PieceWorker e = new PieceWorker("Michael", "Jackson", "555-55-5555", new Date(2, 26, 2024), 10.0, 100);
        assertEquals(10.0, e.getWage(), EPS);
        assertEquals(100, e.getPieces());
        assertEquals(1000.0, e.getPaymentAmount(), EPS);

        e.setWage(12.5);
        e.setPieces(80);
        assertEquals(12.5, e.getWage(), EPS);
        assertEquals(80, e.getPieces());
        assertEquals(1000.0, e.getPaymentAmount(), EPS);

        assertThrows(IllegalArgumentException.class, () -> new PieceWorker("A", "B", "1", new Date(1, 10, 2020), -0.01, 1));
        assertThrows(IllegalArgumentException.class, () -> new PieceWorker("A", "B", "1", new Date(1, 10, 2020), 1.0, -1));

        assertThrows(IllegalArgumentException.class, () -> e.setWage(-0.01));
        assertThrows(IllegalArgumentException.class, () -> e.setPieces(-1));

        String s = e.toString();
        assertTrue(s.toLowerCase().contains("piece worker"));
        assertTrue(s.contains("wage"));
        assertTrue(s.contains("pieces"));
    }

    // =========================================================
    // Payable + Integração (polimorfismo)
    // =========================================================

    // Testa polimorfismo real: mistura vários objetos Payable, soma pagamentos e garante que todos respondem ao contrato corretamente.
    @Test
    @DisplayName("Integração: processa Payable[] polimorficamente e soma pagamentos")
    void integracao_payable_array_polimorfismo() {
        Payable[] payableObjects = new Payable[6];

        payableObjects[0] = new Invoice("01234", "seat", 2, 375.00);
        payableObjects[1] = new Invoice("56789", "tire", 4, 79.95);
        payableObjects[2] = new HourlyEmployee("Karen", "Price", "222-22-2222", new Date(6, 28, 1989), 16.75, 40);
        payableObjects[3] = new CommissionEmployee("Sue", "Jones", "333-33-3333", new Date(1, 26, 2018), 10000, 0.06);
        payableObjects[4] = new BasePlusCommissionEmployee("Bob", "Lewis", "444-44-4444", new Date(1, 25, 2023), 0.04, 5000, 300);
        payableObjects[5] = new PieceWorker("Michael", "Jackson", "555-55-5555", new Date(2, 26, 2024), 10.0, 100);

        double total = 0.0;
        for (Payable p : payableObjects) {
            assertNotNull(p.toString());
            double amt = p.getPaymentAmount();
            assertTrue(amt >= 0.0);
            total += amt;
        }
        assertEquals(3839.80, total, 1e-6);
    }

    // Testa o contrato mínimo da interface Payable: qualquer implementação deve expor getPaymentAmount e funcionar via referência Payable.
    @Test
    @DisplayName("Payable: método existe e funciona por contrato (compilação/execução)")
    void payable_contrato_basico() {
        Payable p = new Invoice("A", "B", 1, 1.0);
        assertEquals(1.0, p.getPaymentAmount(), EPS);
    }
}