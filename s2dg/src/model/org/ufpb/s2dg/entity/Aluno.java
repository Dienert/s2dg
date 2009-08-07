package org.ufpb.s2dg.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "aluno")
public class Aluno implements Serializable {

	public enum SituacaoAcademica {INDEFINIDO, REGULAR, GRADUADO};
	public enum FormaIngresso {VESTIBULAR, DECISAO_CONSEPE,DECISAO_JUDICIAL,MOBILIDADE_INTERNA, VESTIBULAR_EAD_DEMANDA_SOCIAL,
	VESTIBULAR_EAD_PROFESSOR,VESTIBULAR_PROESP_UFPB,VESTIBULAR_PROESP_OUTROS_ORG_PUB,REOPÇÃO_DE_POLO_EAD, EAD_TRANSFERENCIA_PSTV,
	DISCIPLINAS_ISOLADAS_ESPECIAL,TRANSFERENCIA_PSTV, CONVENIO, MUDANCA_DE_CAMPUS_CURSO, GRADUADO, VESTIBULAR_MUDANCA_DE_CURSO,
	TRANSFERENCIA_MUDANCA_DE_CURSO, CONVENIO_MUDANCA_DE_CURSO, TRANSFERENCIA_EX_OFICIO,TRANSFERENCIA_POR_FORCA_LIMINAR,
	TRANSFERENCIA_POR_FORCA_SENTENCA,PEC,REINGRESSO,PROGRAMA_PIANI,VESTIBULAR_2,PROGRAMA_DE_MOBILIDADE_ESTUDANTIL,PEC_MSC,REINGRESSO_POR_DECISAO_DO_CONSEPE,
	REOPÇÃO_MUDANCA_DE_TURNO
	}
	
	private static final long serialVersionUID = 1L;
	private String matricula;
	private Set<AlunoTurma> alunoTurmas = new HashSet<AlunoTurma>(0);
	private Usuario usuario;
	private Curriculo curriculo;
	private SituacaoAcademica situacaoAcademica;
	private FormaIngresso formaIngresso;
	private int matriculasInstitucionais;
	private String dataConclusao;
	private String nomeProva1;
	private String nomeProva2;
	private String nomeProva3;
	private String nomeProva4;
	private String nomeProva5;
	private String nomeProva6;
	private String nomeProva7;
	private String nomeProva8;
	private String nomeProva9;
	private Integer valorProva1;
	private Integer valorProva2;
	private Integer valorProva3;
	private Integer valorProva4;
	private Integer valorProva5;
	private Integer valorProva6;
	private Integer valorProva7;
	private Integer valorProva8;
	private Integer valorProva9;

	public Aluno() {
		situacaoAcademica = SituacaoAcademica.REGULAR;
	}

	public Aluno(String matricula) {
		this.matricula = matricula;
	}

	public Aluno(String matricula, Usuario usuario,
			Set<AlunoTurma> alunoTurmas, Curriculo curriculo, SituacaoAcademica situacaoAcademica,
			FormaIngresso formaIngresso, int matriculasInstitucionais,
			String dataConclusao, String nomeProva1, String nomeProva2, String nomeProva3, String nomeProva4,
			String nomeProva5, String nomeProva6, String nomeProva7, String nomeProva8, String nomeProva9 
			,Integer valorProva1
			, Integer valorProva2, Integer valorProva3, Integer valorProva4, Integer valorProva5, Integer valorProva6, Integer valorProva7
			, Integer valorProva8, Integer valorProva9
			) {
		this.matricula = matricula;
		this.alunoTurmas = alunoTurmas;
		this.usuario = usuario;
		this.curriculo = curriculo;
		this.situacaoAcademica = situacaoAcademica;
		this.formaIngresso = formaIngresso;
		this.matriculasInstitucionais = matriculasInstitucionais;
		this.dataConclusao = dataConclusao;
		this.nomeProva1 = nomeProva1;
		this.nomeProva2 = nomeProva2;
		this.nomeProva3 = nomeProva3;
		this.nomeProva4 = nomeProva4;
		this.nomeProva5 = nomeProva5;
		this.nomeProva6 = nomeProva6;
		this.nomeProva7 = nomeProva7;
		this.nomeProva8 = nomeProva8;
		this.nomeProva9 = nomeProva9;
		this.valorProva1 = valorProva1;
		this.valorProva2 = valorProva2;
		this.valorProva3 = valorProva3;
		this.valorProva4 = valorProva4;
		this.valorProva5 = valorProva5;
		this.valorProva6 = valorProva6;
		this.valorProva7 = valorProva7;
		this.valorProva8 = valorProva8;
		this.valorProva9 = valorProva9;
		
	}

	@Id
	@Column(name = "matricula", unique = true, nullable = false, length = 9)
	@NotNull
	@Length(max = 9)
	public String getMatricula() {
		return this.matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "aluno")
	public Set<AlunoTurma> getAlunoTurmas() {
		return this.alunoTurmas;
	}

	public void setAlunoTurmas(Set<AlunoTurma> alunoTurmas) {
		this.alunoTurmas = alunoTurmas;
	}

	@OneToOne(mappedBy="aluno")
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.ALL})
	@JoinColumn(name = "id_curriculo")
	public Curriculo getCurriculo() {
		return curriculo;
	}
	
	public void setCurriculo(Curriculo curriculo) {
		this.curriculo = curriculo;
	}
	
	@Column(name = "situacao_academica")
	public SituacaoAcademica getSituacaoAcademica() {
		return situacaoAcademica;
	}

	public void setSituacaoAcademica(SituacaoAcademica situacaoAcademica) {
		this.situacaoAcademica = situacaoAcademica;
	}

	@Column(name = "forma_ingresso")
	public FormaIngresso getFormaIngresso() {
		return formaIngresso;
	}

	public void setFormaIngresso(FormaIngresso formaIngresso) {
		this.formaIngresso = formaIngresso;
	}

	@Column(name = "matriculas_institucionais")
	public int getMatriculasInstitucionais() {
		return matriculasInstitucionais;
	}

	public void setMatriculasInstitucionais(int matriculasInstitucionais) {
		this.matriculasInstitucionais = matriculasInstitucionais;
	}
	
	@Column(name = "dt_conclusao")
	public String getDataConclusao() {
		return dataConclusao;
	}

	public void setDataConclusao(String dataConclusao) {
		this.dataConclusao = dataConclusao;
	}

	@Column(name = "nome_prova1")
	public String getNomeProva1() {
		return nomeProva1;
	}

	public void setNomeProva1(String nomeProva1) {
		this.nomeProva1 = nomeProva1;
	}

	@Column(name = "nome_prova2")
	public String getNomeProva2() {
		return nomeProva2;
	}

	public void setNomeProva2(String nomeProva2) {
		this.nomeProva2 = nomeProva2;
	}

	@Column(name = "nome_prova3")
	public String getNomeProva3() {
		return nomeProva3;
	}

	public void setNomeProva3(String nomeProva3) {
		this.nomeProva3 = nomeProva3;
	}

	@Column(name = "nome_prova4")
	public String getNomeProva4() {
		return nomeProva4;
	}

	public void setNomeProva4(String nomeProva4) {
		this.nomeProva4 = nomeProva4;
	}

	@Column(name = "nome_prova5")
	public String getNomeProva5() {
		return nomeProva5;
	}

	public void setNomeProva5(String nomeProva5) {
		this.nomeProva5 = nomeProva5;
	}

	@Column(name = "nome_prova6")
	public String getNomeProva6() {
		return nomeProva6;
	}

	public void setNomeProva6(String nomeProva6) {
		this.nomeProva6 = nomeProva6;
	}

	@Column(name = "nome_prova7")
	public String getNomeProva7() {
		return nomeProva7;
	}

	public void setNomeProva7(String nomeProva7) {
		this.nomeProva7 = nomeProva7;
	}

	@Column(name = "nome_prova8")
	public String getNomeProva8() {
		return nomeProva8;
	}

	public void setNomeProva8(String nomeProva8) {
		this.nomeProva8 = nomeProva8;
	}

	@Column(name = "nome_prova9")
	public String getNomeProva9() {
		return nomeProva9;
	}

	public void setNomeProva9(String nomeProva9) {
		this.nomeProva9 = nomeProva9;
	}

	@Column(name = "valor_prova1")
	public Integer getValorProva1() {
		return valorProva1;
	}

	public void setValorProva1(Integer valorProva1) {
		this.valorProva1 = valorProva1;
	}

	@Column(name = "valor_prova2")
	public Integer getValorProva2() {
		return valorProva2;
	}

	public void setValorProva2(Integer valorProva2) {
		this.valorProva2 = valorProva2;
	}

	@Column(name = "valor_prova3")
	public Integer getValorProva3() {
		return valorProva3;
	}

	public void setValorProva3(Integer valorProva3) {
		this.valorProva3 = valorProva3;
	}

	@Column(name = "valor_prova4")
	public Integer getValorProva4() {
		return valorProva4;
	}

	public void setValorProva4(Integer valorProva4) {
		this.valorProva4 = valorProva4;
	}

	@Column(name = "valor_prova5")
	public Integer getValorProva5() {
		return valorProva5;
	}

	public void setValorProva5(Integer valorProva5) {
		this.valorProva5 = valorProva5;
	}

	@Column(name = "valor_prova6")
	public Integer getValorProva6() {
		return valorProva6;
	}

	public void setValorProva6(Integer valorProva6) {
		this.valorProva6 = valorProva6;
	}

	@Column(name = "valor_prova7")
	public Integer getValorProva7() {
		return valorProva7;
	}

	public void setValorProva7(Integer valorProva7) {
		this.valorProva7 = valorProva7;
	}

	@Column(name = "valor_prova8")
	public Integer getValorProva8() {
		return valorProva8;
	}

	public void setValorProva8(Integer valorProva8) {
		this.valorProva8 = valorProva8;
	}

	@Column(name = "valor_prova9")
	public Integer getValorProva9() {
		return valorProva9;
	}

	public void setValorProva9(Integer valorProva9) {
		this.valorProva9 = valorProva9;
	}


	
}
