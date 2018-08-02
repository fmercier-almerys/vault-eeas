# Json Message Encryption library

Encrypt and decrypt sensible Json fields in Json messages.

This Maven project is composed of some modules.

* encryption-vault-api: Core module which contains all source code in charge of encryption/decryption of a Json message. Offers also some abstraction level with Interface for vault driver and a custom encryption engine.
* springboot-vault-sample: Springboot application sample exposes a Rest controller and makes call to a deployed Vault server to encrypt/decrypt json messages. It relies on https://www.vaultproject.io/[Hashicorp vault] project and https://www.vaultproject.io/docs/secrets/transit/index.html[transit secrets engine].
* spring-vault-restdriver: SpringBoot vault driver implementation using `RestTemplate`
* vertx-vault-restdriver: Vertx vault driver implementation using `Http Client Core`

Based on the Encryption as a Service function of Vault project (`Transit Secret Engine)` it allows generic Rest message to be protected.
You can use another Encrypt Engine, just implement `EncryptEngine` interface. Same way to use a custom Vault Driver.

You need also to configure all sensible fields you want to anonymize. See the configuration part of this documentation.
Under the hood it uses `JsonPointer` but to allow bulk encryption/decryption inside a Json tree, `JsonPointer` syntax is enhanced by adding wildcard `*`:
Wildcard allows you to traverse Json array to target all elements.

The library contains a classic synchrone API and another asynchrone. Choose to use it according to your application framework.

For example :
```json
{
	"id": 1,
	"name": "foo",
	"surname": "foo",
	"address": {
		"street":"mainstreet",
		"city": {
			"name": "St Francisco",
			"items": [
				{
					"raw": "raw value",
					"raw2": "raw2 value"
				},
				{
					"raw": "2nd raw value",
					"raw2": "2nd raw2 value"
				}
			]
		}
	},
	"items": ["54ff", "fs45", "fsd54fsd"],
	"items2": ["1111", "22222", "33333"]
}
```

```Yaml
    - /name
    - /surname
    - /items/0
    - /items2/*
    - /address/street
    - /address/city/items/*/raw
    - /address/city/items/0/raw2
    - /address/city/name
```

```Json
{
    "id": 1,
    "name": "vault:v1:NDd8qd5PqBFTBXaLse+eZm28qg6w8H2nPd87jEzxti4=",
    "surname": "vault:v1:9r3noDus34FJPAYySqgNgEjDXikO46t7eV31625B1pE=",
    "address": {
        "street": "vault:v1:kLUajDMc/iF298Vd6qp5WYU7JPxJcaX9smfKVgl1/quT4mrccnc=",
        "city": {
            "name": "vault:v1:VS+9OK4rLbXXxyziWBhr9BOJ/HnAOOEfDg6THbvWoEf6mdvc",
            "items": [
                {
                    "raw": "vault:v1:HXDgUNLPhzPYowrvKMHKz/i+8idcW2AOXZKLX1/lig==",
                    "raw2": "vault:v1:iMNEEDiDEQSvSy7WinOlCNpzvpLDkGWSmqLJ+tvcgFc="
                },
                {
                    "raw": "vault:v1:gSd70SRN2KVD1QCd5dnBXii30YvlC3kI6YatGM1wPw==",
  					"raw2": "2nd raw2 value"
                }
            ]
        }
    },
    "items": [
        "vault:v1:1z/xbExInVCPZKs8+XQISwfqPgwOuFY41zORMoU=",
        "fs45",
        "fsd54fsd"
    ],
    "items2": [
        "vault:v1:keePc40CCPsJTBkCAyjsSntSLPzHNvplp5BLNHk=",
        "vault:v1:9ofaXvrvo6wjpcSQ59i5Qa8HUjuuRzEHWMfxd7M=",
        "vault:v1:DwTyJwTEOvs2bnoCkcqibDQgdOWHwWYTz4Ylznc="
    ]
}
```

You can target only primitive values not a whole json object.

To test this project, simply deploy Vault server.
Up to you to execute the vault server in dev mode or more securely with a Consul server behind.
A sample deployement is done using Vagrant with associated resources located into `/Vagrant` folder.


## Build

This application require :

* Maven 3
* JDK 8


## Configuration 

Up to you to define how required properties can be configured. It must implements `com.github.fabmrc.eaas.api.VaultProperties` interface.

Currently :

Example :

```YAML
vault:
  host: "http://192.168.50.11:8200"
  key: "mykey"
  decrypt-endpoint: "v1/transit/decrypt"
  encrypt-endpoint: "v1/transit/encrypt"
  token: "9bf5c884-1479-bae6-cefb-3d54ccfc0cd7"
  fields:
    - /name
    - /surname
    - /items/0
    - /items2/*
    - /address/street
    - /address/city/items/*/raw
    - /address/city/items/0/raw2
    - /address/city/name
```
