
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Programa {

    public static void main(String[] args) throws IOException {
        List<Aluno> alunos = new ArrayList<>();
        String caminhoAlunos = "src/alunos.csv";
        String caminhoResumo = "src/resumo.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(caminhoAlunos))) {
            String linha;
            boolean isFirstLine = true;
            while ((linha = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] dados = linha.split(";");
                if (dados.length != 3) {
                    continue;
                }
                try {
                    int matricula = Integer.parseInt(dados[0].trim());
                    String nome = dados[1].trim();
                    double nota = Double.parseDouble(dados[2].trim().replace(",", "."));
                    alunos.add(new Aluno(matricula, nome, nota));
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int totalTurma = alunos.size();
        int aprovados = (int) alunos.stream().filter(aluno -> aluno.getNota() >= 6).count();
        int reprovados = totalTurma - aprovados;
        double menorNota = alunos.stream().mapToDouble(Aluno::getNota).min().orElse(0);
        double maiorNota = alunos.stream().mapToDouble(Aluno::getNota).max().orElse(0);
        double mediaGeral = alunos.stream().mapToDouble(Aluno::getNota).average().orElse(0);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoResumo))) {
            bw.write("TotalAlunosTurma;Aprovados;Reprovados;MenorNota;MaiorNota;MediaGeral\n");
            bw.write(String.format("%d;%d;%d;%.2f;%.2f;%.2f\n", totalTurma, aprovados, reprovados, menorNota, maiorNota, mediaGeral));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
