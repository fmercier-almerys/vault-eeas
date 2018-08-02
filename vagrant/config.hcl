storage "consul" {
  address = "192.168.50.11:8500"
  path    = "vault/"
}

listener "tcp" {
 address     = "192.168.50.11:8200"
 tls_disable = 1
}

