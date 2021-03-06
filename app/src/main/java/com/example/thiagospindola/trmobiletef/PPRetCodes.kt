package com.example.thiagospindola.trmobiletef

class PPRetCodes{
    companion object {
        val PP_OK: Int = 0
        val PP_PROCESSING: Int = 1
        val PP_NOTIFY: Int = 2
        val PP_F1: Int = 4
        val PP_F2: Int = 5
        val PP_F3: Int = 6
        val PP_F4: Int = 7
        val PP_BACKSP: Int = 8

        //Erros basicos na biblioteca
        val PP_INVCALL: Int = 10
        val PP_INVPARM: Int = 11
        val PP_TIMEOUT: Int = 12
        val PP_CANCEL: Int = 13
        val PP_ALREADYOPEN: Int = 14
        val PP_NOTOPEN: Int = 15
        val PP_EXECERR: Int = 16
        val PP_INVMODEL: Int = 17
        val PP_NOFUNC: Int = 18
        val PP_TABEXP: Int = 20
        val PP_TABERR: Int = 21
        val PP_NOAPPLIC: Int = 22

        //Erros de comunicacao/protocolo com o pinpad
        val PP_PORTERR: Int = 30
        val PP_COMMERR: Int = 31
        val PP_UNKNOWNSTAT: Int = 32
        val PP_RSPERR: Int = 33
        val PP_COMMTOUT: Int = 34

        //Erros basicos reportados pelo pinpad
        val PP_INTERR: Int = 40
        val PP_MCDATAERR: Int = 41
        val PP_ERRPIN: Int = 42
        val PP_NOCARD: Int = 43
        val PP_PINBUSY: Int = 44

        //Erros de processamento de cartao com chip (SAM)
        val PP_SAMERR: Int = 50
        val PP_NOSAM: Int = 51
        val PP_SAMINV: Int = 52

        //Erros de processamento de cartao com chip com contato
        val PP_DUMBCARD: Int = 60
        val PP_ERRCARD: Int = 61
        val PP_CARDINV: Int = 62
        val PP_CARDBLOCKED: Int = 63
        val PP_CARDNAUTH: Int = 64
        val PP_CARDEXPIRED: Int = 65
        val PP_CARDERRSTRUCT: Int = 66
        val PP_CARDINVALIDAT: Int = 67
        val PP_CARDPROBLEMS: Int = 68
        val PP_CARDINVDATA: Int = 69
        val PP_CARDAPPNAV: Int = 70
        val PP_CARDAPPNAUT: Int = 71
        val PP_NOBALANCE: Int = 72
        val PP_LIMITEXC: Int = 73
        val PP_CARDNOTEFFECT: Int = 74
        val PP_VCINVCURR: Int = 75
        val PP_ERRFALLBACK: Int = 76

        //Erros de processamento de cartão com chip sem contato
        val PP_CTLSSMULTIPLE: Int = 80
        val PP_CTLSSCOMMERR: Int = 81
        val PP_CTLSSINVALIDAT: Int = 82
        val PP_CTLSSPROBLEMS: Int = 83
        val PP_CTLSSAPPNAV: Int = 84
        val PP_CTLSSAPPNAUT: Int = 85



        val RETC_ERRPINPAD: String = "ERRO PINPAD"
        val RETC_READERROR: String = "ERRO DE LEITURA TENTE NOVAMENTE"
        val RETC_CARDREMOVED: String = "CARTAO REMOVIDO"
        val RETC_PINPADBUSY: String = "PINPAD OCUPADO TENTE NOVAMENTE"
        val RETC_SAMERROR: String = "PROBL.MOD.SAM"
        val RETC_CARDERROR: String = "CARTAO COM ERRO OU MAL INSERIDO"
        val RETC_INVALIDCARD: String = "CARTAO INVALIDO"
        val RETC_BLOCKEDCARD: String = "CARTAO BLOQUEADO"
        val RETC_EXPIREDCARD: String = "CARTAO VENCIDO"
        val RETC_INVALIDATEDCARD: String = "CARTAO INVALIDADO"
        val RETC_INVALIDMODE: String = "MODO INVALIDO, PASSE O CARTAO"
        val RETC_CARDNOTACCEPTED: String = "CARTAO NAO ACEITO"
        val RETC_NOBALANCE: String = "SALDO INSUFICIENTE NO MOEDEIRO"
        val RETC_LIMITEXC: String = "EXCEDE LIMITE POR TRANSACAO"
        val RETC_CARDNOTEFFEC: String = "CARTAO NAO EFETIVO"
        val RETC_INVCURRENCY: String = "CARTAO POSSUI MOEDA INVALIDA"
        val RETC_MULTIPLECARDS: String = "MAIS DE UM CARTAO APRESENTADO"
        val RETC_CTLSCOMMERROR: String = "ERRO DE COMUNICACAO COM O CARTAO"
        val RETC_CTLSINVCARD: String = "CARTAO INVALIDADO USE CHIP/TARJA"
        val RETC_CTLSPROBLEMS: String = "CARTAO INVALIDO, USE CHIP/TARJA"
        val RETC_CLTSINVMODE: String = "MODO INVALIDO, USE CHIP/TARJA"
        val RETC_CTLSNOTACCEPTED: String = "NAO ACEITO, USE CHIP/TARJA"


        val RETPP_READERR: String = "ERRO DE LEITURA\n" + "TENTE NOVAMENTE"
        val RETPP_REMOVEDCARD: String = "CARTAO REMOVIDO"
        val RETPP_CARDERR: String = "CARTAO COM ERRO\n" + "OU MAL INSERIDO"
        val RETPP_INVALIDCARD: String = "CARTAO\n" + "    INVALIDO"
        val RETPP_BLOCKEDCARD: String = "CARTAO\n" + "BLOQUEADO"
        val RETPP_EXPIREDCARD: String = "CARTAO\n" + "VENCIDO"
        val RETPP_INVALIDATEDCARD: String = "CARTAO\n" + "INVALIDADO"
        val RETPP_INVALIDMODE: String = "MODO INVALIDO\n" + "PASSE O CARTAO"
        val RETPP_CARDNOTACCEPTED: String = "CARTAO NAO\n" + "  ACEITO"
        val RETPP_NOBALANCE: String = "SALDO\n" + "INSUFICIENTE"
        val RETPP_LIMITEXC: String = "EXCEDE LIMITE\n" + "POR TRANSACAO"
        val RETPP_CARDNOEFFEC: String = "CARTAO\n" + "NAO EFETIVO"
        val RETPP_INVCURRENCY: String = "CARTAO POSSUI\n" + "MOEDA INVALIDA"
        val RETPP_MULTIPLECARDS: String = "MAIS DE UM\n" + "CARTAO"
        val RETPP_CTLSCOMMERROR: String = "ERRO COMUNICACAO\n" + "  COM O CARTAO"
        val RETPP_CTLSINVCARD: String = "CART. INVALIDADO\n" + " USE CHIP/TARJA"
        val RETPP_CTLSPROBLEMS: String = "CARTAO INVALIDO\n" + " USE CHIP/TARJA"
        val RETPP_CTLSINVMODE: String = "MODO INVALIDO\n" + "USE CHIP/TARJA"
        val RETPP_CTLSNOTACCEPTED: String = "NAO ACEITO\n" + "USE CHIP/TARJA"
    }
}